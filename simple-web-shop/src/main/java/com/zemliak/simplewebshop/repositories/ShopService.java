package com.zemliak.simplewebshop.repositories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zemliak.simplewebshop.core.Core;
import com.zemliak.simplewebshop.exceptions.ItemLocalizationNotFound;
import com.zemliak.simplewebshop.exceptions.ItemValidationFailed;
import com.zemliak.simplewebshop.exceptions.UserNotFoundException;
import com.zemliak.simplewebshop.models.LocalizedObject;
import com.zemliak.simplewebshop.models.good.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService implements IShopService {

    @Autowired
    private ShopRepository repository;

    private final Gson gson = new Gson();

    @Override
    public List<Good> getAllGoodsByLanguageTag(String languageTag) {
        //setTestData();

        List<Good> goods = new ArrayList<>();

        for (GoodDao dao: repository.findAll()) {
            try {
                goods.add(daoToGood(dao, languageTag));
            } catch (ItemLocalizationNotFound e){
                System.out.println(e.getMessage());
            }
        }

        return goods;
    }

    @Override
    public void saveGoods(List<Good> goods, String languageTag) {
        for (Good good: goods) {
            saveGood(good, languageTag);
        }
    }

    @Override
    public void saveGood(Good good, String languageTag) {
        if(!validateAddedShopItem(good)){
            throw new ItemValidationFailed();
        }

        repository.save(goodToDao(good, languageTag));
    }

    @Override
    public void updateGood(Good good, String languageTag) {
        if(!validateAddedShopItem(good)){
            throw new ItemValidationFailed();
        }

        Optional<GoodDao> optionalGood = repository.findById(good.getId());

        if(optionalGood.isPresent()){
            GoodDao daoGoodToUpdate = optionalGood.get();

            String newName = setOrUpdatePropertyAndSetToJson(
                    daoGoodToUpdate.getName(),
                    new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                    new GoodsNameLocalization(good.getName(), languageTag),
                    languageTag
            );
            String newPrice = setOrUpdatePropertyAndSetToJson(
                    daoGoodToUpdate.getPrice(),
                    new TypeToken<ArrayList<GoodsPriceLocalization>>(){}.getType(),
                    new GoodsPriceLocalization(good.getPrice(), languageTag),
                    languageTag
            );
            String newDescription = setOrUpdatePropertyAndSetToJson(
                    daoGoodToUpdate.getDescription(),
                    new TypeToken<ArrayList<GoodsDescriptionLocalization>>(){}.getType(),
                    new GoodsDescriptionLocalization(good.getDescription(), languageTag),
                    languageTag
            );

            daoGoodToUpdate.setName(newName);
            daoGoodToUpdate.setPrice(newPrice);
            daoGoodToUpdate.setDescription(newDescription);
            daoGoodToUpdate.setPictureSource(good.getPictureSource());
            daoGoodToUpdate.setUpdateDate(Core.convertToFormattedString(LocalDateTime.now()));

            repository.save(daoGoodToUpdate);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public Good findGoodByIdAndLanguageTag(Long id, String languageTag) {
        Optional<GoodDao> optionalGood = repository.findById(id);
        if(optionalGood.isPresent()){
            return daoToGood(optionalGood.get(), languageTag);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void deleteGoodById(Long id) {
        Optional<GoodDao> optionalGood = repository.findById(id);
        if(optionalGood.isPresent()){
            repository.delete(optionalGood.get());
        } else {
            throw new UserNotFoundException();
        }
    }

    private boolean validateAddedShopItem(Good good) {
        return good.getName() != null &&
                !good.getName().isEmpty() &&
                good.getPrice() > 0;
    }

    private GoodDao goodToDao(Good good, String languageTag){
        GoodsNameLocalization goodsNameLocalization = new GoodsNameLocalization(
                good.getName(),
                languageTag
        );
        List<GoodsNameLocalization> nameLocalizationList = new ArrayList<>();
        nameLocalizationList.add(goodsNameLocalization);

        GoodsPriceLocalization goodsPriceLocalization = new GoodsPriceLocalization(
                good.getPrice(),
                languageTag
        );
        List<GoodsPriceLocalization> priceLocalizationList = new ArrayList<>();
        priceLocalizationList.add(goodsPriceLocalization);

        GoodsDescriptionLocalization goodsDescriptionLocalization = new GoodsDescriptionLocalization(
                good.getDescription(),
                languageTag
        );
        List<GoodsDescriptionLocalization> descriptionLocalizationList = new ArrayList<>();
        descriptionLocalizationList.add(goodsDescriptionLocalization);

        return new GoodDao(
                gson.toJson(nameLocalizationList),
                gson.toJson(priceLocalizationList),
                gson.toJson(descriptionLocalizationList),
                good.getPictureSource()
        );
    }

    private Good daoToGood(GoodDao dao, String languageTag){
        Type nameListType = new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType();
        Type priceListType = new TypeToken<ArrayList<GoodsPriceLocalization>>(){}.getType();
        Type descriptionListType = new TypeToken<ArrayList<GoodsDescriptionLocalization>>(){}.getType();

        List<GoodsNameLocalization> names = gson.fromJson(dao.getName(), nameListType);
        List<GoodsPriceLocalization> prices = gson.fromJson(dao.getPrice(), priceListType);
        List<GoodsDescriptionLocalization> descriptions = gson.fromJson(dao.getDescription(), descriptionListType);

        Optional<GoodsNameLocalization> optionalName = names.stream().filter(e -> e.getLanguageTag().equals(languageTag)).findFirst();
        Optional<GoodsPriceLocalization> optionalPrice = prices.stream().filter(e -> e.getLanguageTag().equals(languageTag)).findFirst();
        Optional<GoodsDescriptionLocalization> optionalDescription = descriptions.stream().filter(e -> e.getLanguageTag().equals(languageTag)).findFirst();

        if(optionalName.isPresent() && optionalPrice.isPresent() && optionalDescription.isPresent()){
            return new Good(
                    dao.getId(),
                    optionalName.get().getItemName(),
                    optionalPrice.get().getItemPrice(),
                    optionalDescription.get().getItemDescription(),
                    dao.getPictureSource(),
                    dao.getCreationDate(),
                    dao.getUpdateDate()
            );
        }

        throw new ItemLocalizationNotFound();
    }

    private String setOrUpdatePropertyAndSetToJson(String jsonProperty, Type type, LocalizedObject newLocalization, String languageTag){
        if(jsonProperty == null || jsonProperty.isEmpty()){
            List<LocalizedObject> localizedObjects = new ArrayList<>();
            localizedObjects.add(newLocalization);
            return gson.toJson(localizedObjects);
        }

        List<LocalizedObject> localizedObjects = gson.fromJson(jsonProperty, type);

        Optional<LocalizedObject> optionalByLanguage = localizedObjects.stream().filter(e -> e.getLanguageTag().equals(languageTag)).findFirst();

        if(optionalByLanguage.isPresent()){
            Collections.replaceAll(localizedObjects, optionalByLanguage.get(), newLocalization);
        } else {
            localizedObjects.add(newLocalization);
        }

        return gson.toJson(localizedObjects);
    }

    private void setTestData(){
        repository.deleteAll();

        repository.save(new GoodDao(
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock", "ru"),
                        "ru"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsPriceLocalization>>(){}.getType(),
                        new GoodsPriceLocalization(1500, "ru"),
                        "ru"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock of 1500", "ru"),
                        "ru"
                ),
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSObl2jeVvp5HZWDk7ddTTXDkVYMCYJRKPMRgqeMz2dLByZDxLy17fepPyI8fmGkCgT12R0593&usqp=CAc"
        ));

        repository.save(new GoodDao(
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock", "ru"),
                        "ru"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsPriceLocalization>>(){}.getType(),
                        new GoodsPriceLocalization(2500, "ru"),
                        "ru"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock of 2500", "ru"),
                        "ru"
                ),
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIlzpaQa97DOs4uhsiXM5aLa5KkqfBzPSZ1-vJYVJHv0dOABUiMlObKsi4hhO2wbe4_5ctmdM&usqp=CAc"
        ));

        repository.save(new GoodDao(
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock", "ru"),
                        "ru"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsPriceLocalization>>(){}.getType(),
                        new GoodsPriceLocalization(4500, "ru"),
                        "ru"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock of 4500", "ru"),
                        "ru"
                ),
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2fQj9M--NSiAH4wTfAKrfl_uGxL6chtmGEytMvWF9CKHQHo07zPcAvLDLPIqVm6vche5pGj_B&usqp=CAc"
        ));

        repository.save(new GoodDao(
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock", "en"),
                        "en"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsPriceLocalization>>(){}.getType(),
                        new GoodsPriceLocalization(15, "en"),
                        "en"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock of 15", "en"),
                        "en"
                ),
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSIlzpaQa97DOs4uhsiXM5aLa5KkqfBzPSZ1-vJYVJHv0dOABUiMlObKsi4hhO2wbe4_5ctmdM&usqp=CAc"
        ));

        repository.save(new GoodDao(
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock", "en"),
                        "en"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsPriceLocalization>>(){}.getType(),
                        new GoodsPriceLocalization(50, "en"),
                        "en"
                ),
                setOrUpdatePropertyAndSetToJson(
                        null,
                        new TypeToken<ArrayList<GoodsNameLocalization>>(){}.getType(),
                        new GoodsNameLocalization("Reebock of 50", "en"),
                        "en"
                ),
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2fQj9M--NSiAH4wTfAKrfl_uGxL6chtmGEytMvWF9CKHQHo07zPcAvLDLPIqVm6vche5pGj_B&usqp=CAc"
        ));
    }
}
