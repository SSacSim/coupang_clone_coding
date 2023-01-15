package com.example.demo.src.cart;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.cart.model.*;
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
public class CartProvider {

    private final CartDao cartDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CartProvider(CartDao cartDao, JwtService jwtService) {
        this.cartDao = cartDao;
        this.jwtService = jwtService;
    }

    public List<GetCartRes> getCartItems(int userId) throws BaseException{
        try{
            List<GetCartRes> getCartItems = cartDao.getCartItems(userId);
            return getCartItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetWishItemsRes> getWishItems(int userId) throws BaseException{
        try{
            List<GetWishItemsRes> getWishItems = cartDao.getWishItems(userId);
            return getWishItems;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOrderRes getOrderRes(int userId) throws BaseException {
        try {
            if(cartDao.checkCart(userId) == 0) {
                throw new BaseException(EMPTY_CART);
            }
            List<GetCartRes> getCartItems = cartDao.getCartItems(userId);
            int addressId = cartDao.getAddressId(userId);
            int paymentId = cartDao.getPaymentId(userId);
            GetOrderRes getOrderRes = cartDao.getOrderRes(userId, addressId, paymentId, getCartItems);
            return getOrderRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
