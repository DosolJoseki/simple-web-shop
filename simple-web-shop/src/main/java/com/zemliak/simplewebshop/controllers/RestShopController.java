package com.zemliak.simplewebshop.controllers;

import com.zemliak.simplewebshop.core.Messages;
import com.zemliak.simplewebshop.exceptions.UserNotFoundException;
import com.zemliak.simplewebshop.models.good.Good;
import com.zemliak.simplewebshop.repositories.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestShopController {

    @Autowired
    private Messages messages;

    @Autowired
    private IShopService shopService;

    @GetMapping("api/getAllGoodsByLanguageTag")
    public List<Good> getAllGoods(String languageTag) {
        return shopService.getAllGoodsByLanguageTag(languageTag);
    }

    @GetMapping("api/findGoodsById")
    public Good findGoodsById(Long id, String languageTag) {
        return shopService.findGoodByIdAndLanguageTag(id, languageTag);
    }

    @GetMapping("api/updateGoods")
    public String updateGoods(Good good, String languageTag) {
        try{
            shopService.updateGood(good, languageTag);
            return messages.get("message_update_successed");
        } catch (UserNotFoundException e){
            return messages.get("error_message_user_not_found");
        }
    }

    @GetMapping("api/createGoods")
    public String createGoods(Good good, String languageTag) {
        try {
            shopService.saveGood(good, languageTag);
            return messages.get("message_create_successed");
        } catch (UserNotFoundException e){
            return messages.get("error_message_user_not_found");
        }
    }

    @GetMapping("api/deleteGoodById")
    public String deleteGoodById(Long id) {
        try{
            shopService.deleteGoodById(id);
            return messages.get("message_delete_successed");
        } catch (UserNotFoundException e) {
            return messages.get("error_message_user_not_found");
        }
    }
}
