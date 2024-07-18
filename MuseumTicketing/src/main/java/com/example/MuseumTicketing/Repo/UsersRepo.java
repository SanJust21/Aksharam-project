package com.example.MuseumTicketing.Repo;


import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Model.Users;
import com.example.MuseumTicketing.Model.Role;
import com.example.MuseumTicketing.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmployeeId(String employeeId);

    Users findByRole(Role role);

    @Query("SELECT MAX(u.employeeId) FROM Users u WHERE u.role IN (:roles)")
    String findMaxEmployeeId(@Param("roles") List<Role> roles);


    List<Users> findAllByRole(Role role);

    boolean existsByEmployeeId(String newEmployeeId);

    List<Users> findAllByRoleIn(List<Role> roles);

    Optional<Users> findByName(String name);

    void deleteByName(String name);
}
