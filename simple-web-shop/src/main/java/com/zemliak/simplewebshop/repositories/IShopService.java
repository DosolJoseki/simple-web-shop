package com.zemliak.simplewebshop.repositories;

import com.zemliak.simplewebshop.models.good.Good;

import java.util.List;

public interface IShopService {
    public List<Good> getAllGoodsByLanguageTag(String languageTag);
    public void saveGoods(List<Good> goods, String languageTag);
    public void saveGood(Good good, String languageTag);
    public void updateGood(Good good, String languageTag);
    public Good findGoodByIdAndLanguageTag(Long id, String languageTag);
    public void deleteGoodById(Long id);
}
