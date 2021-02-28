package com.zemliak.simplewebshop.repositories;

import com.zemliak.simplewebshop.models.good.GoodDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<GoodDao, Long> {

}
