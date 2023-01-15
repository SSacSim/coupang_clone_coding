package com.example.demo.src.cart;

import com.example.demo.src.cart.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CartDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkUserId(int userId) {
        String checkQuery = "select exists(select userId from User where userId = ?)";
        int checkParams = userId;
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int checkItemId(int itemId) {
        String checkQuery = "select exists(select itemId from Item where itemId = ?)";
        int checkParams = itemId;
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int checkCart(int userId) {
        String checkQuery = "select exists(select itemId from Cart where userId = ? and status='A')";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, userId);
    }

    public int checkCart(int userId, int itemId) {
        String checkQuery = "select exists(select itemId from Cart where userId = ? and itemId = ? and status='A')";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, userId, itemId);
    }

    public void patchCart(int userId, int itemId, int count) {
        String patchQuery = "update Cart set count = ? where userId = ? and itemId = ?";
        this.jdbcTemplate.update(patchQuery, count, userId, itemId);
    }

    public void deleteCart(int userId, int itemId) {
        String delteQuery = "update Cart set status='I' where userId = ? and itemId = ?";
        this.jdbcTemplate.update(delteQuery, userId, itemId);
    }

    public int getAddressId(int userId) {
        String getQuery = "select IFNULL((SELECT addressId from Address where userId= ? and isDefault='Y'), 0)";
        return this.jdbcTemplate.queryForObject(getQuery, int.class, userId);
    }

    public int getPaymentId(int userId) {
        String getQuery = "select IFNULL((SELECT paymentId from Payment where userId= ? and isDefault='Y'), 0)";
        return this.jdbcTemplate.queryForObject(getQuery, int.class, userId);
    }

    public List<GetCartRes> getCartItems(int userId) {
        String getCartQuery = "select c.itemId, itemName, IFNULL(i.rocket, \"null\") as rocket, img.itemImgUrl, c.count itemCount,\n" +
                "       (select IF(i.discount=0, i.price, i.couPrice)) price,\n" +
                "       (select IF(u.wowMem='Y', i.wowPrice, 0)) wowPrice\n" +
                "from Cart c\n" +
                "inner join Item i on i.itemId = c.itemId\n" +
                "inner join User u on u.userId = c.userId\n" +
                "inner join ItemMainImg img on img.itemId = c.itemId and itemImgSeq=1\n" +
                "where c.userId = ? and c.status='A'";
        return this.jdbcTemplate.query(getCartQuery,
                (rs, rowNum) -> new GetCartRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getString("rocket"),
                        rs.getString("itemImgUrl"),
                        rs.getInt("itemCount"),
                        ( rs.getInt("wowPrice") == 0 ? rs.getInt("price") : rs.getInt("wowPrice") ) * rs.getInt("itemCount")),
                userId);
    }

    public List<GetWishItemsRes> getWishItems(int userId) {
        String getWishQuery = "select li.itemId, i.itemName, i.discount, i.price, i.couPrice, i.wowPrice, i.rocket, img.itemImgUrl\n" +
                "from LikeItem li\n" +
                "inner join Item i on i.itemId = li.itemId\n" +
                "inner join ItemMainImg img on img.itemId = i.itemId and img.itemImgSeq=1\n" +
                "where userId = ? and li.status='A'";
        return this.jdbcTemplate.query(getWishQuery,
                (rs, rowNum) -> new GetWishItemsRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getInt("discount"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getString("rocket"),
                        rs.getString("itemImgUrl")),
                userId);
    }

    public GetOrderRes getOrderRes(int userId, int aId, int pId, List<GetCartRes> cartItems) {
        String getQuery = "select a.addressId, a.recipient, a.address, a.phone, a.comment as deliveryReq, p.paymentId, p.type as paymentType,\n" +
                "    CASE\n" +
                "        WHEN p.type = \"coupay\" THEN u.couPay\n" +
                "        WHEN p.type = \"bank\" THEN b.bankName\n" +
                "        WHEN p.type = \"card\" THEN c.company\n" +
                "    END as payInfo\n" +
                "from Address a\n" +
                "inner join Payment p on p.userId = a.userId and p.paymentId= " + pId +
                "\nleft outer join Bank b on b.paymentId = p.paymentId\n" +
                "left outer join Card c on c.paymentId = p.paymentId\n" +
                "left outer join User u on u.userId = p.userId\n" +
                "where a.userId = ? and a.addressId = " + aId;

        String getAddressQuery = "select a.addressId, a.recipient, a.address, a.phone, a.comment as deliveryReq\n" +
                "from Address a\n" +
                "where a.userId = ? and a.addressId = " + aId;
        String getPaymentQuery = "select p.type as paymentType, \n" +
                "    CASE\n" +
                "        WHEN p.type = \"coupay\" THEN u.couPay\n" +
                "        WHEN p.type = \"bank\" THEN b.bankName\n" +
                "        WHEN p.type = \"card\" THEN c.company\n" +
                "    END as payInfo\n" +
                "from Payment p\n" +
                "left outer join Bank b on b.paymentId = p.paymentId\n" +
                "left outer join Card c on c.paymentId = p.paymentId\n" +
                "left outer join User u on u.userId = p.userId\n" +
                "where p.userId = ? and p.paymentId = " + pId;

        int itemCount = cartItems.size();
        String[] itemNames = new String[itemCount];
        String[] rockets = new String[itemCount];
        int[] itemCounts = new int[itemCount];
        int[] itemPrices = new int[itemCount];

        int itemCnt = 0;
        for(int i=0; i<itemCount; i++) {
            itemNames[i] = cartItems.get(i).getItemName();
            rockets[i] = cartItems.get(i).getRocket();
            if(rockets[i].equals("")) {
                rockets[i] = "normal";
            }
            itemCounts[i] = cartItems.get(i).getItemCount();
            itemCnt += itemCounts[i];
            itemPrices[i] = cartItems.get(i).getPrice();
        }

        int totalCount = itemCnt;

        if(aId == 0 && pId == 0) {
            return new GetOrderRes(userId, aId, "", "배송지를 선택해주세요.", "", "", pId, "결제수단을 등록해주세요.", "",
                    totalCount, itemNames, rockets, itemCounts, itemPrices);
        } else if(aId == 0) {
            System.out.println(aId + ", " + pId);
            return this.jdbcTemplate.queryForObject(getPaymentQuery,
                    (rs2, rowNum2) -> new GetOrderRes(
                            userId, aId, "", "배송지를 선택해주세요.", "", "",
                            pId, rs2.getString("paymentType"), rs2.getString("payInfo"),
                            totalCount, itemNames, rockets, itemCounts, itemPrices)
                    ,userId);
        } else if(pId == 0) {
            return this.jdbcTemplate.queryForObject(getAddressQuery,
                    (rs3, rowNum3) -> new GetOrderRes(
                            userId, aId, rs3.getString("recipient"), rs3.getString("address"), rs3.getString("phone"),
                            rs3.getString("deliveryReq"), pId, "결제수단을 등록해주세요.", "",
                            totalCount, itemNames, rockets, itemCounts, itemPrices)
                    ,userId);
        }
        return this.jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new GetOrderRes(
                        userId,
                        rs.getInt("addressId"),
                        rs.getString("recipient"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("deliveryReq"),
                        rs.getInt("paymentId"),
                        rs.getString("paymentType"),
                        rs.getString("payInfo"),
                        totalCount,
                        itemNames,
                        rockets,
                        itemCounts,
                        itemPrices)
                ,userId);
    }
}
