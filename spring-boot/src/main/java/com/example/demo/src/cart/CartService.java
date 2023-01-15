package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.src.cart.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CartService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CartDao cartDao;
    private final CartProvider cartProvider;
    private final JwtService jwtService;

    @Autowired
    public CartService(CartDao cartDao, CartProvider cartProvider, JwtService jwtService) {
        this.cartDao = cartDao;
        this.cartProvider = cartProvider;
        this.jwtService = jwtService;

    }

    //POST
    public void patchCart(int userId, int itemId, int count) throws BaseException {
        if(cartDao.checkUserId(userId) == 0){ // 잘못된 Jwt인 경우(해당 User 없음)
            throw new BaseException(INVALID_USER_JWT);
        }
        if(cartDao.checkItemId(itemId) == 0){ // 잘못된 ItemId인 경우(해당 ItemId 없음)
            throw new BaseException(INVALID_ITEMID_ERROR);
        }
        if(cartDao.checkCart(userId, itemId) == 1) { // Cart 안에 이미 있는 경우
            try {
                cartDao.patchCart(userId, itemId, count);
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        } else { // Cart 안에 해당 상품이 없는 경우
            throw new BaseException(PATCH_CART_INVALID_ITEMID);
        }
    }

    //POST
    public void deleteCart(int userId, int itemId) throws BaseException {
        if(cartDao.checkUserId(userId) == 0){ // 잘못된 Jwt인 경우(해당 User 없음)
            throw new BaseException(INVALID_USER_JWT);
        }
        if(cartDao.checkItemId(itemId) == 0){ // 잘못된 ItemId인 경우(해당 ItemId 없음)
            throw new BaseException(INVALID_ITEMID_ERROR);
        }
        if(cartDao.checkCart(userId, itemId) == 1) { // Cart 안에 이미 있는 경우
            cartDao.deleteCart(userId, itemId);
        } else { // Cart 안에 해당 상품이 없는 경우
            throw new BaseException(DELETE_CART_INVALID_ITEMID);
        }
    }

}
