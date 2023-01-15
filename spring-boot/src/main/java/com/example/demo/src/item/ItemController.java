package com.example.demo.src.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.item.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/items")
public class ItemController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ItemProvider itemProvider;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final JwtService jwtService;


    public ItemController(ItemProvider itemProvider, ItemService itemService, JwtService jwtService){
        this.itemProvider = itemProvider;
        this.itemService = itemService;
        this.jwtService = jwtService;
    }

    /**
     * 상품 검색 API
     * [GET] /items/search?searchWord=
     * @return BaseResponse<List<GetItemsRes>>
     */
    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<List<GetItemsRes>> getSearchItems(@RequestParam(required = true) String searchWord) {
        try {
            List<GetItemsRes> getSearchItems;
            if(jwtService.getJwt() == null) {
                getSearchItems = itemProvider.getSearchItems(searchWord);
            } else {
                int userIdByJwt = jwtService.getUserId();
                getSearchItems = itemProvider.getSearchItems(userIdByJwt, searchWord);
            }
            return new BaseResponse<>(getSearchItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상품 검색 자동완성 API
     * [GET] /items?search=
     * @return BaseResponse<List<GetSearchRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetSearchRes>> getSearchRes(@RequestParam(required = true) String search) {
        try {
            List<GetSearchRes> getSearchResList = itemProvider.getSearchRes(search);
            return new BaseResponse<>(getSearchResList);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 최근 검색어 조회 API
     * [GET] /items/search/history
     * @return BaseResponse<List<String>>
     */
    @ResponseBody
    @GetMapping("/search/history")
    public BaseResponse<List<String>> getSearchHistory() {
        try {
            int userIdByJwt = jwtService.getUserId();
            List<String> searchHistory = itemProvider.getSearchHistory(userIdByJwt);
            return new BaseResponse<>(searchHistory);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 할인 상품 조회 API
     * [GET] /items/discount-item
     * @return BaseResponse<List<GetItemsRes>>
     */
    @ResponseBody
    @GetMapping("/discount-item")
    public BaseResponse<List<GetItemsRes>> getDiscountItems() {
        try{
            List<GetItemsRes> getDiscountItems = itemProvider.getDiscountItems();
            return new BaseResponse<>(getDiscountItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 최근 찾던 상품의 연관 상품 (최근 조회한 카테고리의 관련 상품) API
     * [GET] /items/related-item
     * @return BaseResponse<List<GetItemsRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/related-item")
    public BaseResponse<List<GetItemsRes>> getRelatedItems(@RequestBody RecentCategoryInfo recentCategoryInfo) {
        try{
            int categoryId = recentCategoryInfo.getRecentCategoryId();
            List<GetItemsRes> getCategoryItems = itemProvider.getCategoryItems(categoryId, 20);
            return new BaseResponse<>(getCategoryItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로켓 배송 상품 조회 API
     * [GET] /items/rocket-delivery
     * @return BaseResponse<List<GetItemsRes>>
     */
    @ResponseBody
    @GetMapping("/rocket-delivery")
    public BaseResponse<List<GetItemsRes>> getRocketDeliveryItems() {
        try{
            List<GetItemsRes> getRocketDeliveryItems = itemProvider.getRocketDeliveryItems();
            return new BaseResponse<>(getRocketDeliveryItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로켓 프레시 상품 조회 API
     * [GET] /items/rocket-fresh
     * @return BaseResponse<List<GetItemsRes>>
     */
    @ResponseBody
    @GetMapping("/rocket-fresh")
    public BaseResponse<List<GetItemsRes>> getRocketFreshItems() {
        try{
            List<GetItemsRes> getRocketFreshItems = itemProvider.getRocketFreshItems();
            return new BaseResponse<>(getRocketFreshItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 요즘 뜨는 {카테고리} 상품 조회 API
     * [GET] /items/hot-item/:categoryId
     * @return BaseResponse<List<GetItemsRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/hot-item/{categoryId}")
    public BaseResponse<List<GetItemsRes>> getHotItems(@PathVariable("categoryId") int categoryId) {
        try{
            List<GetItemsRes> getCategoryItems = itemProvider.getCategoryItems(categoryId, 20);
            return new BaseResponse<>(getCategoryItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 카테고리 전체 목록 조회 API
     * [GET] /items/category
     * @return BaseResponse<List<String>>
     */
    @ResponseBody
    @GetMapping("/category")
    public BaseResponse<List<String>> getCategories() {
        try{
            List<String> getCategoryNames = itemProvider.getCategories();
            return new BaseResponse<>(getCategoryNames);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 카테고리별 상품 조회 API
     * [GET] /items/category/:categoryId
     * @return BaseResponse<List<GetItemsRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/category/{categoryId}")
    public BaseResponse<List<GetItemsRes>> getCategoryItems(@PathVariable("categoryId") int categoryId) {
        try{
            List<GetItemsRes> getCategoryItems = itemProvider.getCategoryItems(categoryId);
            return new BaseResponse<>(getCategoryItems);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상품 상세페이지 조회 API
     * [GET] /items/:itemId
     * @return BaseResponse<Item>
     */
    //Query String
    @ResponseBody
    @GetMapping("/{itemId}")
    public BaseResponse<Item> getItem(@PathVariable("itemId") int itemId) {
        try{
            Item item = itemProvider.getItem(itemId);
            return new BaseResponse<>(item);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 리뷰 조회 API
     * [GET] /items/:itemId/review?sort=best (베스트순)
     * [GET] /items/:itemId/review?sort=date (최신순)
     * @return BaseResponse<List<Review>>
     */
    @ResponseBody
    @GetMapping("/{itemId}/review")
    public BaseResponse<List<Review>> getItemReview(@PathVariable("itemId") int itemId, @RequestParam(required = true) String sort) {
        try{
            int orderType = 0;
            if(sort.equals("date")) {
                orderType = 1;
            }
            List<Review> reviewList = itemProvider.getReviewList(itemId, orderType);
            return new BaseResponse<>(reviewList);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상품 리뷰의 사진만 조회 API
     * [GET] /items/:itemId/review/photo
     * @return BaseResponse<PhotoReviewInfo>
     */
    @ResponseBody
    @GetMapping("/{itemId}/review/photo")
    public BaseResponse<PhotoReviewInfo> getPhotoReviewInfo(@PathVariable("itemId") int itemId) {
        try{
            PhotoReviewInfo photoReviewInfo = itemProvider.getPhotoReviewInfo(itemId);
            return new BaseResponse<>(photoReviewInfo);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상품 찜 API
     * [POST] /items/{itemId}/like
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{itemId}/like")
    public BaseResponse<String> postWishItem(@PathVariable("itemId") int itemId){
        try{
            //jwt에서 id 추출
            int userIdByJwt = jwtService.getUserId();

            itemService.postWishItem(itemId, userIdByJwt);

            String result = "찜한 상품에 저장되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상품 찜 취소 API
     * [DELETE] /items/{itemId}/unlike
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{itemId}/unlike")
    public BaseResponse<String> deleteWishItem(@PathVariable("itemId") int itemId){
        try{
            //jwt에서 id 추출
            int userIdByJwt = jwtService.getUserId();

            itemService.deleteWishItem(itemId, userIdByJwt);

            String result = "취소되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 리뷰 좋아요 API
     * [POST] /items/{itemId}/{reviewId}/like
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{itemId}/{reviewId}/like")
    public BaseResponse<String> postReviewLike(@PathVariable("itemId") int itemId, @PathVariable("reviewId") int reviewId){
        try{
            //jwt에서 id 추출
            int userIdByJwt = jwtService.getUserId();

            itemService.postReviewLike(itemId, reviewId, userIdByJwt);

            String result = "평가를 주고 받으면 리뷰어 랭킹 점수가 올라갑니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 리뷰 좋아요 취소 API
     * [DELETE] /items/{itemId}/{reviewId}/unlike
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{itemId}/{reviewId}/unlike")
    public BaseResponse<String> deleteReviewLike(@PathVariable("itemId") int itemId, @PathVariable("reviewId") int reviewId){
        try{
            //jwt에서 id 추출
            int userIdByJwt = jwtService.getUserId();

            itemService.deleteReviewLike(itemId, reviewId, userIdByJwt);

            String result = "리뷰 평가가 취소되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 장바구니 담기 API
     * [POST] /items/{itemId}/cart
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{itemId}/cart")
    public BaseResponse<String> postCart(@PathVariable("itemId") int itemId, @RequestBody Cart cart){
        try{
            //jwt에서 id 추출
            int userIdByJwt = jwtService.getUserId();
            int userId = cart.getUserId();

            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(itemId != cart.getItemId()){
                return new BaseResponse<>(POST_CART_INVALID_ITEMID);
            }

            itemService.postCart(userId, cart.getItemId(), cart.getCount());

            String result = "장바구니에 상품이 담겼습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
