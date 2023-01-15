package com.example.demo.src.item;

import com.example.demo.src.item.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ItemDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkItemId(int itemId) {
        String checkQuery = "select exists(select itemId from Item where itemId = ?)";
        int checkParams = itemId;
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int checkReviewId(int itemId, int reviewId) {
        String checkQuery = "select exists(select reviewId from Review where reviewId = ? and itemid = ? )";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, reviewId, itemId);
    }

    public int checkCategoryId(int categoryId) {
        String checkQuery = "select exists(select categoryId from Category where categoryId = ?)";
        int checkParams = categoryId;
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int checkUserId(int userId) {
        String checkQuery = "select exists(select userId from User where userId = ?)";
        int checkParams = userId;
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int checkWishList(int itemId, int userId) {
        String checkQuery = "select exists(select itemId from LikeItem where itemId = ? and userId = ?)";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, itemId, userId);
    }

    public int checkReviewLike(int reviewId, int userId) {
        String checkQuery = "select exists(select userId from ReviewLike where reviewId = ? and userId = ?)";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, reviewId, userId);
    }

    public int checkCart(int userId, int itemId) {
        String checkQuery = "select IFNULL((SELECT count from Cart where userId= ? and itemId= ? ), 0) as count";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, userId, itemId);
    }

    public int checkSearchHistory(int userId, String search) {
        String checkQuery = "select IFNULL((SELECT userId from SearchHistory where userId= ? and word= ? ), 0) as count";
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, userId, search);
    }

    public List<String> getSearchHistory(int userId){
        String getHistoryQuery = "select word from SearchHistory where userId= ? order by updatedAt desc;";
        List<String> searchHistory = this.jdbcTemplate.query(getHistoryQuery,
                (rs, rowNum) -> rs.getString("word"), userId);
        return searchHistory;
    }

    public List<GetSearchRes> getSearchRes(String search) {
        String getSearchQuery = "select itemId, itemName\n" +
                "from Item i\n" +
                "where itemName LIKE N'%" + search + "%'";
        return this.jdbcTemplate.query(getSearchQuery,
                (rs, rowNum) -> new GetSearchRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName")
                ));
    }

    public List<GetItemsRes> getSearchItems(String search) {
        String getItemsQuery = "select i.itemId, i.itemName, img.itemImgUrl, i.categoryId, i.rocket, i.shippingCost, i.price, i.couPrice, i.wowPrice, i.discount, i.brandName,\n" +
                "       (select IFNULL(AVG(reviewScore),0) from Review where itemId = i.itemId) as starScore,\n" +
                "       (select count(*) from Review where itemId = i.itemId) as reviewCnt\n" +
                "from Item i\n" +
                "inner join ItemMainImg img on i.itemId = img.itemId and img.itemImgSeq=1\n" +
                "where i.itemName LIKE N'%" + search + "%'";
        return this.jdbcTemplate.query(getItemsQuery,
                (rs, rowNum) -> new GetItemsRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemImgUrl"),
                        rs.getInt("categoryId"),
                        rs.getString("rocket"),
                        rs.getInt("shippingCost"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getInt("discount"),
                        rs.getString("brandName"),
                        rs.getInt("starScore"),
                        rs.getInt("reviewCnt")
                ));
    }

    public List<GetItemsRes> getDiscountItems() {
        String getItemsQuery = "select i.itemId, i.itemName, img.itemImgUrl, i.categoryId, i.rocket, i.shippingCost, i.price, i.couPrice, i.wowPrice, i.discount, i.brandName, " +
                "       (select IFNULL(AVG(reviewScore),0) from Review where itemId = i.itemId) as starScore, " +
                "       (select count(*) from Review where itemId = i.itemId) as reviewCnt\n" +
                "from Item i\n" +
                "inner join ItemMainImg img on i.itemId = img.itemId and img.itemImgSeq=1\n" +
                "where i.discount > 0 order by i.discount desc limit 30";
        return this.jdbcTemplate.query(getItemsQuery,
                (rs, rowNum) -> new GetItemsRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemImgUrl"),
                        rs.getInt("categoryId"),
                        rs.getString("rocket"),
                        rs.getInt("shippingCost"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getInt("discount"),
                        rs.getString("brandName"),
                        rs.getInt("starScore"),
                        rs.getInt("reviewCnt")
                ));
    }

    public List<GetItemsRes> getRocketDeliveryItems() {
        String getItemsQuery = "select i.itemId, i.itemName, img.itemImgUrl, i.categoryId, i.rocket, i.shippingCost, i.price, i.couPrice, i.wowPrice, i.discount, i.brandName, " +
                "       (select IFNULL(AVG(reviewScore),0) from Review where itemId = i.itemId) as starScore, " +
                "       (select count(*) from Review where itemId = i.itemId) as reviewCnt\n" +
                "from Item i\n" +
                "inner join ItemMainImg img on i.itemId = img.itemId and img.itemImgSeq=1\n" +
                "where i.rocket=\"delivery\"";
        return this.jdbcTemplate.query(getItemsQuery,
                (rs, rowNum) -> new GetItemsRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemImgUrl"),
                        rs.getInt("categoryId"),
                        rs.getString("rocket"),
                        rs.getInt("shippingCost"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getInt("discount"),
                        rs.getString("brandName"),
                        rs.getInt("starScore"),
                        rs.getInt("reviewCnt")
                ));
    }

    public List<GetItemsRes> getRocketFreshItems() {
        String getItemsQuery = "select i.itemId, i.itemName, img.itemImgUrl, i.categoryId, i.rocket, i.shippingCost, i.price, i.couPrice, i.wowPrice, i.discount, i.brandName, " +
                "       (select IFNULL(AVG(reviewScore),0) from Review where itemId = i.itemId) as starScore, " +
                "       (select count(*) from Review where itemId = i.itemId) as reviewCnt\n" +
                "from Item i\n" +
                "inner join ItemMainImg img on i.itemId = img.itemId and img.itemImgSeq=1\n" +
                "where i.rocket=\"fresh\"";
        return this.jdbcTemplate.query(getItemsQuery,
                (rs, rowNum) -> new GetItemsRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemImgUrl"),
                        rs.getInt("categoryId"),
                        rs.getString("rocket"),
                        rs.getInt("shippingCost"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getInt("discount"),
                        rs.getString("brandName"),
                        rs.getInt("starScore"),
                        rs.getInt("reviewCnt")
                ));
    }

    public List<String> getCategories(){
        String getCategoriesQuery = "select categoryName from Category order by categoryId";
        List<String> getCategoryNames = this.jdbcTemplate.query(getCategoriesQuery,
                (rs, rowNum) -> rs.getString("categoryName"));
        return getCategoryNames;
    }

    public List<GetItemsRes> getCategoryItems(int categoryId) {
        String getItemsQuery = "select i.itemId, i.itemName, img.itemImgUrl, i.categoryId, i.rocket, i.shippingCost, i.price, i.couPrice, i.wowPrice, i.discount, i.brandName, " +
                "       (select IFNULL(AVG(reviewScore),0) from Review where itemId = i.itemId) as starScore, " +
                "       (select count(*) from Review where itemId = i.itemId) as reviewCnt\n" +
                "from Item i\n" +
                "inner join ItemMainImg img on i.itemId = img.itemId and img.itemImgSeq=1\n" +
                "where categoryId = ?";
        int getItemsParams = categoryId;
        return this.jdbcTemplate.query(getItemsQuery,
                (rs, rowNum) -> new GetItemsRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemImgUrl"),
                        rs.getInt("categoryId"),
                        rs.getString("rocket"),
                        rs.getInt("shippingCost"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getInt("discount"),
                        rs.getString("brandName"),
                        rs.getInt("starScore"),
                        rs.getInt("reviewCnt")
                ),
                getItemsParams);
    }

    public List<GetItemsRes> getCategoryItems(int categoryId, int limit) {
        String getItemsQuery = "select i.itemId, i.itemName, img.itemImgUrl, i.categoryId, i.rocket, i.shippingCost, i.price, i.couPrice, i.wowPrice, i.discount, i.brandName, " +
                "       (select IFNULL(AVG(reviewScore),0) from Review where itemId = i.itemId) as starScore, " +
                "       (select count(*) from Review where itemId = i.itemId) as reviewCnt\n" +
                "from Item i\n" +
                "inner join ItemMainImg img on i.itemId = img.itemId and img.itemImgSeq=1\n" +
                "where categoryId = ?\n" +
                "order by i.createdAt desc limit ?";
        int getItemsParams = categoryId;
        return this.jdbcTemplate.query(getItemsQuery,
                (rs, rowNum) -> new GetItemsRes(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemImgUrl"),
                        rs.getInt("categoryId"),
                        rs.getString("rocket"),
                        rs.getInt("shippingCost"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getInt("discount"),
                        rs.getString("brandName"),
                        rs.getInt("starScore"),
                        rs.getInt("reviewCnt")
                ),
                getItemsParams, limit);
    }

    public Item getItem(int itemId) {
        String getItemBoardQuery = "select itemBoard from Item where itemId = " + itemId;
        String s = this.jdbcTemplate.queryForObject(getItemBoardQuery, String.class);
        String[] sArr = s.split(",");
        String[][] itemBoard = new String[sArr.length/2][2];
        int cnt = 0;
        for(int i=0; i<sArr.length/2; i++) {
            for(int j=0; j<2; j++) {
                itemBoard[i][j] = sArr[cnt++];
            }
        }

        String getMainImgQuery = "select itemImgUrl from ItemMainImg where itemId = " + itemId + "\n order by itemImgSeq";
        String[] mainImgUrlArr = this.jdbcTemplate.query(getMainImgQuery, (rs, rowNum) -> rs.getString("itemImgUrl")).toArray(new String[0]);

        String getSubImgQuery = "select subImgUrl from ItemSubImg where itemId = " + itemId + "\n order by subImgSeq";
        String[] subImgUrlArr = this.jdbcTemplate.query(getSubImgQuery, (rs, rowNum) -> rs.getString("subImgUrl")).toArray(new String[0]);

        String getItemQuery = "select i.itemId, itemName, categoryId, rocket, shippingCost, price, couPrice, wowPrice, discount, sellerName, brandName, " +
                "       (select IFNULL(AVG(reviewScore),0) from Review where itemId = i.itemId) as starScore, " +
                "       (select count(*) from Review where itemId = i.itemId) as reviewCnt\n" +
                "from Item i where itemId = " + itemId;
        return this.jdbcTemplate.queryForObject(getItemQuery,
                (rs, rowNum) -> new Item(
                        rs.getInt("itemId"),
                        rs.getString("itemName"),
                        rs.getInt("categoryId"),
                        rs.getString("rocket"),
                        rs.getInt("shippingCost"),
                        rs.getInt("price"),
                        rs.getInt("couPrice"),
                        rs.getInt("wowPrice"),
                        rs.getInt("discount"),
                        rs.getString("sellerName"),
                        rs.getString("brandName"),
                        itemBoard,
                        rs.getInt("starScore"),
                        rs.getInt("reviewCnt"),
                        mainImgUrlArr,
                        subImgUrlArr)
        );
    }

    //GET
    public List<Review> getReviewList(int itemId, int orderType) {
        String getReviewImgQuery = "select reviewImgUrl from ReviewImg where reviewId = ? order by reviewImgSeq";

        String getReviewQuery = "select r.reviewId, r.itemId, IFNULL(u.profileImgUrl, \"null\") profileImgUrl, " +
                "u.userName, r.reviewScore, DATE(r.createdAt) AS date, i.sellerName, i.itemName, r.reviewTxt, " +
                "(select count(*) from ReviewLike rl where rl.reviewId = r.reviewId) as likeCount\n" +
                "from Review r\n" +
                "inner join User u on u.userId = r.userId\n" +
                "inner join Item i on i.itemId = r.itemId\n" +
                "where r.itemId = " + itemId;
        if(orderType == 0)
            getReviewQuery += "\norder by likeCount desc";
        else
            getReviewQuery += "\norder by r.createdAt desc";

        return this.jdbcTemplate.query(getReviewQuery,
                (rs, rowNum) -> new Review(
                        rs.getInt("reviewId"),
                        rs.getInt("itemId"),
                        rs.getString("profileImgUrl"),
                        rs.getString("userName"),
                        rs.getInt("reviewScore"),
                        rs.getString("date"),
                        rs.getString("sellerName"),
                        rs.getString("itemName"),
                        this.jdbcTemplate.query(getReviewImgQuery, (rs2, rowNum2) -> rs2.getString("reviewImgUrl"), rs.getInt("reviewId")).toArray(new String[0]),
                        rs.getString("reviewTxt"),
                        rs.getInt("likeCount"))
        );
    }

    public PhotoReviewInfo getPhotoReviewInfo(int itemId) {
        String getPhotoQuery = "select img.reviewImgUrl\n" +
                "from Review r\n" +
                "    inner join ReviewImg img on r.reviewId = img.reviewId\n" +
                "where r.itemId = " + itemId +
                "\norder by r.createdAt desc limit 4";
        String[] reviewImgArr = this.jdbcTemplate.query(getPhotoQuery, (rs, rowNum) -> rs.getString("reviewImgUrl")).toArray(new String[0]);

        String getPhotoCountQuery = "select count(img.reviewImgUrl) as photoCount\n" +
                "from Review r\n" +
                "inner join ReviewImg img on r.reviewId = img.reviewId\n" +
                "where r.itemId = " + itemId;
        int photoCnt = this.jdbcTemplate.queryForObject(getPhotoCountQuery, int.class);
        PhotoReviewInfo photoReviewInfo = new PhotoReviewInfo(reviewImgArr, photoCnt);
        return photoReviewInfo;
    }

    //POST
    public void postWishItem(int itemId, int userId){
        String createWishQuery = "insert into LikeItem (userId, itemId) VALUES (?,?)";
        this.jdbcTemplate.update(createWishQuery, userId, itemId);
    }

    //POST
    public void postReviewLike(int reviewId, int userId){
        String createWishQuery = "insert into ReviewLike (reviewId, userId) VALUES (?,?)";
        this.jdbcTemplate.update(createWishQuery, reviewId, userId);
    }

    //POST
    public void postCart(int userId, int itemId, int count){
        String postCartQuery = "insert into Cart (userId, itemId, count) VALUES (?,?,?)";
        this.jdbcTemplate.update(postCartQuery, userId, itemId, count);
    }

    public void updateSearchHistory(int userId, String search){
        String updateQuery = "update SearchHistory set updatedAt = now() where userId = ? and word = ?";
        this.jdbcTemplate.update(updateQuery, userId, search);
    }

    public void createSearchHistory(int userId, String search){
        String createHistoryQuery = "insert into SearchHistory (userId, word) VALUES (?,?)";
        this.jdbcTemplate.update(createHistoryQuery, userId, search);
    }

    //PATCH
    public void patchCart(int userId, int itemId, int preCount, int count){
        String patchCartQuery = "update Cart set count = ? where userId = ? and itemId = ? and status='A'";
        this.jdbcTemplate.update(patchCartQuery, preCount+count, userId, itemId);
        String patchStatusQuery = "update Cart set count = ? , status='A' where userId = ? and itemId = ? and status='I'";
        this.jdbcTemplate.update(patchStatusQuery, count, userId, itemId);
    }

    //PATCH
    public int patchWishList(int itemId, int userId, String wish){
        String checkStatusQuery = "select status from LikeItem where userId = ? and itemId = ?";
        String statusCheck = this.jdbcTemplate.queryForObject(checkStatusQuery, (rs, rowNum) -> rs.getString("status"), userId, itemId);
        if(statusCheck.equals(wish)) {
            return 0;
        } else {
            String patchWishQuery = "update LikeItem set status = '" + wish + "' where userId = ? and itemId = ?";
            this.jdbcTemplate.update(patchWishQuery, userId, itemId);
            return 1;
        }
    }

    //PATCH
    public int patchReviewLike(int reviewId, int userId, String sign){
        String checkStatusQuery = "select status from ReviewLike where userId = ? and reviewId = ?";
        String statusCheck = this.jdbcTemplate.queryForObject(checkStatusQuery, (rs, rowNum) -> rs.getString("status"), userId, reviewId);
        if(statusCheck.equals(sign)) {
            return 0;
        } else {
            String patchReviewLikeQuery = "update ReviewLike set status = '" + sign + "' where userId = ? and reviewId = ?";
            this.jdbcTemplate.update(patchReviewLikeQuery, userId, reviewId);
            return 1;
        }
    }

}