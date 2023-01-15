package com.example.demo.src.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.cart.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/carts")
public class CartController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CartProvider cartProvider;
    @Autowired
    private final CartService cartService;
    @Autowired
    private final JwtService jwtService;


    public CartController(CartProvider cartProvider, CartService cartService, JwtService jwtService){
        this.cartProvider = cartProvider;
        this.cartService = cartService;
        this.jwtService = jwtService;
    }

    /**
     * 장바구니 조회 API
     * [GET] /carts
     * @return BaseResponse<List<GetCartRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCartRes>> getCartItems() {
        try {
            int userIdByJwt = jwtService.getUserId();

            List<GetCartRes> getCartRes = cartProvider.getCartItems(userIdByJwt);

            return new BaseResponse<>(getCartRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 장바구니 상품 수량 변경 API
     * [PATCH] /carts/{itemId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{itemId}")
    public BaseResponse<String> patchCart(@PathVariable("itemId") int itemId, @RequestBody Cart cart){
        try{
            //jwt에서 id 추출
            int userIdByJwt = jwtService.getUserId();

            if(cart.getUserId() != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(itemId != cart.getItemId()){
                return new BaseResponse<>(PATCH_CART_INVALID_ITEMID);
            }

            cartService.patchCart(userIdByJwt, cart.getItemId(), cart.getCount());

            String result = "상품 수량이 변경되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 장바구니 상품 삭제 API
     * [DELETE] /carts/{itemId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{itemId}")
    public BaseResponse<String> deleteCart(@PathVariable("itemId") int itemId, @RequestBody Cart cart){
        try{
            //jwt에서 id 추출
            int userIdByJwt = jwtService.getUserId();

            if(cart.getUserId() != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(itemId != cart.getItemId()){
                return new BaseResponse<>(DELETE_CART_INVALID_ITEMID);
            }

            cartService.deleteCart(userIdByJwt, cart.getItemId());

            String result = "장바구니에서 상품이 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 찜한 상품 조회 API
     * [GET] /carts/wish-item
     * @return BaseResponse<List<GetWishItemRes>>
     */
    @ResponseBody
    @GetMapping("/wish-item")
    public BaseResponse<List<GetWishItemsRes>> getWishItems() {
        try {
            int userIdByJwt = jwtService.getUserId();

            List<GetWishItemsRes> getWishItems = cartProvider.getWishItems(userIdByJwt);

            return new BaseResponse<>(getWishItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 장바구니 전체 결제 요청 API
     * [GET] /carts/buy
     * @return BaseResponse<GetOrderRes>
     */
    @ResponseBody
    @GetMapping("/buy")
    public BaseResponse<GetOrderRes> getCartOrder() {
        try {
            int userIdByJwt = jwtService.getUserId();

            GetOrderRes getOrderRes = cartProvider.getOrderRes(userIdByJwt);
            return new BaseResponse<>(getOrderRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
