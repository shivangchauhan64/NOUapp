package com.app.nouapp.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.nouapp.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

}
