package com.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.entity.Clazz;

public interface JpaClazzRepository extends JpaRepository<Clazz, Integer> {

	@Modifying
	@Transactional
	@Query(value = "Alter table Clazz auto_increment = 1", nativeQuery = true)
	void resetAutoIncrement();

	@Modifying
	@Transactional
	@Query(value = "truncate table Clazz", nativeQuery = true)
	void truncate();

}
