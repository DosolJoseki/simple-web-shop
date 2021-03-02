package com.zemliak.simplewebshop.controllers;

import com.google.gson.Gson;
import com.zemliak.simplewebshop.core.Messages;
import com.zemliak.simplewebshop.exceptions.ItemValidationFailed;
import com.zemliak.simplewebshop.models.good.Good;
import com.zemliak.simplewebshop.repositories.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Controller
public class ShopController {

    @Autowired
    private Messages messages;

    @Autowired
    private IShopService shopService;

    @Autowired
    private HttpServletRequest request;

    private Gson gson = new Gson();

    @GetMapping("/goods")
    public String showGoods(Model model) {
        List<Good> goods = shopService.getAllGoodsByLanguageTag(getRequestLocaleLanguage());
        model.addAttribute("goods", goods);
        model.addAttribute("title", messages.get("title_shop"));
        return "goods";
    }

    @GetMapping("/goods/{id}")
    public String showGoodsById(Model model, @PathVariable(value = "id") Long id) {
        Good goods = shopService.findGoodByIdAndLanguageTag(id, getRequestLocaleLanguage());
        model.addAttribute("goods", goods);
        model.addAttribute("title", messages.get("title_current_item"));
        return "goods-description";
    }

    @GetMapping("/goods/add")
    public String addGoods(Model model) {
        model.addAttribute("title", messages.get("title_add_item"));
        return "goods-add";
    }

    @PostMapping("/goods/add")
    public String addGoodsPost(
            @RequestParam String name,
            @RequestParam(required = false) Integer price,
            @RequestParam String description,
            @RequestParam String picUrl,
            Model model) {

        if (price == null || price < 1) {
            throw new ItemValidationFailed();
        }

        String languageTag = getRequestLocaleLanguage();

        Good good = new Good(
                name,
                price,
                description,
                picUrl
        );
        shopService.saveGood(good, languageTag);
        return "redirect:/goods";
    }

    private String getRequestLocaleLanguage() {
        Locale locale = request.getLocale();
        if (locale == null) {
            locale = new Locale("en");
        }

        return locale.getLanguage();
    }
}
