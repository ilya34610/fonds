package ru.pssbd.fonds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pssbd.fonds.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Short> {
}
