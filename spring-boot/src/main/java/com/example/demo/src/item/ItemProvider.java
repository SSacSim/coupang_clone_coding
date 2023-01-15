package com.example.demo.src.item;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.item.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ItemProvider {

    private final ItemDao itemDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ItemProvider(ItemDao itemDao, JwtService jwtService) {
        this.itemDao = itemDao;
        this.jwtService = jwtService;
    }

    public List<GetSearchRes> getSearchRes(String search) throws BaseException{
        try{
            List<GetSearchRes> getSearchResList = itemDao.getSearchRes(search);
            return getSearchResList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemsRes> getSearchItems(String search) throws BaseException{
        try{
            List<GetItemsRes> getSearchItems = itemDao.getSearchItems(search);
            return getSearchItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemsRes> getSearchItems(int userId, String search) throws BaseException{
        try{
            if(itemDao.checkSearchHistory(userId, search) == 0) {
                itemDao.createSearchHistory(userId, search);
            } else {
                itemDao.updateSearchHistory(userId, search);
            }
            List<GetItemsRes> getSearchItems = itemDao.getSearchItems(search);
            return getSearchItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<String> getSearchHistory(int userId) throws BaseException{
        try{
            List<String> getSearchHistory = itemDao.getSearchHistory(userId);
            return getSearchHistory;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemsRes> getDiscountItems() throws BaseException{
        try{
            List<GetItemsRes> getDiscountItems = itemDao.getDiscountItems();
            return getDiscountItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemsRes> getRocketDeliveryItems() throws BaseException{
        try{
            List<GetItemsRes> getRocketDeliveryItems = itemDao.getRocketDeliveryItems();
            return getRocketDeliveryItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemsRes> getRocketFreshItems() throws BaseException{
        try{
            List<GetItemsRes> getRocketFreshItems = itemDao.getRocketFreshItems();
            return getRocketFreshItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<String> getCategories() throws BaseException{
        try{
            List<String> getCategoryNames = itemDao.getCategories();
            return getCategoryNames;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemsRes> getCategoryItems(int categoryId) throws BaseException{
        if(itemDao.checkCategoryId(categoryId) == 0)
            throw new BaseException(INVALID_CATEGORYID_ERROR);
        
        try{
            List<GetItemsRes> getCategoryItems = itemDao.getCategoryItems(categoryId);
            return getCategoryItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetItemsRes> getCategoryItems(int categoryId, int limit) throws BaseException{
        if(itemDao.checkCategoryId(categoryId) == 0)
            throw new BaseException(INVALID_CATEGORYID_ERROR);

        try{
            List<GetItemsRes> getCategoryItems = itemDao.getCategoryItems(categoryId, limit);
            return getCategoryItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public Item getItem(int itemId) throws BaseException{
        if(itemDao.checkItemId(itemId) == 0)
            throw new BaseException(INVALID_ITEMID_ERROR);

        try{
            Item item = itemDao.getItem(itemId);
            return item;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<Review> getReviewList(int itemId, int orderType) throws BaseException{
        if(itemDao.checkItemId(itemId) == 0)
            throw new BaseException(INVALID_ITEMID_ERROR);

        try{
            List<Review> reviewList = itemDao.getReviewList(itemId, orderType);
            return reviewList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PhotoReviewInfo getPhotoReviewInfo(int itemId) throws BaseException{
        if(itemDao.checkItemId(itemId) == 0)
            throw new BaseException(INVALID_ITEMID_ERROR);

        try{
            PhotoReviewInfo photoReviewInfo = itemDao.getPhotoReviewInfo(itemId);
            return photoReviewInfo;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
