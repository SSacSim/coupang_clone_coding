package com.example.demo.src.item;

import com.example.demo.config.BaseException;
import com.example.demo.src.item.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ItemService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ItemDao itemDao;
    private final ItemProvider itemProvider;
    private final JwtService jwtService;

    @Autowired
    public ItemService(ItemDao itemDao, ItemProvider itemProvider, JwtService jwtService) {
        this.itemDao = itemDao;
        this.itemProvider = itemProvider;
        this.jwtService = jwtService;

    }

    //POST
    public void postWishItem(int itemId, int userId) throws BaseException {
        if(itemDao.checkUserId(userId) == 0){ // 잘못된 Jwt인 경우(해당 User 없음)
            throw new BaseException(INVALID_USER_JWT);
        }
        if(itemDao.checkItemId(itemId) == 0){ // 잘못된 ItemId인 경우(해당 ItemId 없음)
            throw new BaseException(INVALID_ITEMID_ERROR);
        }
        if(itemDao.checkWishList(itemId, userId) == 1) { // LikeItem 안에 이미 있는 경우 (A / I)
            if(itemDao.patchWishList(itemId, userId, "A") == 0) {
                throw new BaseException(DUPLICATED_ITEM_WISH);
            }
        } else {
            try {
                itemDao.postWishItem(itemId, userId);
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        }
    }

    //POST
    public void postReviewLike(int itemId, int reviewId, int userId) throws BaseException {
        if(itemDao.checkReviewId(itemId, reviewId) == 0){ // 잘못된 ReviewId인 경우(해당 ReviewId 없음)
            throw new BaseException(INVALID_REVIEWID_ERROR);
        }
        if(itemDao.checkUserId(userId) == 0){ // 잘못된 Jwt인 경우(해당 User 없음)
            throw new BaseException(INVALID_USER_JWT);
        }
        if(itemDao.checkReviewLike(reviewId, userId) == 1) { // ReviewLike 안에 이미 있는 경우 (A / I)
            if(itemDao.patchReviewLike(reviewId, userId, "A") == 0) {
                throw new BaseException(DUPLICATED_REVIEW_LIKE);
            }
        } else {
            try {
                itemDao.postReviewLike(reviewId, userId);
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        }
    }

    //DELETE
    public void deleteWishItem(int itemId, int userId) throws BaseException {
        if(itemDao.checkUserId(userId) == 0){ // 잘못된 Jwt인 경우(해당 User 없음)
            throw new BaseException(INVALID_USER_JWT);
        }
        if(itemDao.checkItemId(itemId) == 0){ // 잘못된 ItemId인 경우(해당 ItemId 없음)
            throw new BaseException(INVALID_ITEMID_ERROR);
        }
        if(itemDao.checkWishList(itemId, userId) == 1) { // LikeItem 안에 이미 있는 경우 (A / I)
            if(itemDao.patchWishList(itemId, userId, "I") == 0) {
                throw new BaseException(INVALID_WISH_ITEM);
            }
        } else {
            throw new BaseException(INVALID_WISH_ITEM);
        }
    }

    //DELETE
    public void deleteReviewLike(int itemId, int reviewId, int userId) throws BaseException {
        if(itemDao.checkReviewId(itemId, reviewId) == 0){ // 잘못된 ReviewId인 경우(해당 ReviewId 없음)
            throw new BaseException(INVALID_REVIEWID_ERROR);
        }
        if(itemDao.checkUserId(userId) == 0){ // 잘못된 Jwt인 경우(해당 User 없음)
            throw new BaseException(INVALID_USER_JWT);
        }
        if(itemDao.checkReviewLike(reviewId, userId) == 1) { // ReviewLike 안에 이미 있는 경우 (A / I)
            if(itemDao.patchReviewLike(reviewId, userId, "I") == 0) {
                throw new BaseException(DUPLICATED_REVIEW_LIKE);
            }
        } else {
            throw new BaseException(INVALID_REVIEW_LIKE);
        }
    }

    //POST
    public void postCart(int userId, int itemId, int count) throws BaseException {
        if(itemDao.checkItemId(itemId) == 0){ // 잘못된 ItemId인 경우(해당 ItemId 없음)
            throw new BaseException(INVALID_ITEMID_ERROR);
        }
        try{
            int preCount = itemDao.checkCart(userId, itemId);
            if(preCount == 0) {
                itemDao.postCart(userId, itemId, count);
            } else {
                itemDao.patchCart(userId, itemId, preCount, count);
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
