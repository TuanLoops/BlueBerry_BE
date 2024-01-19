package com.blueberry.repository;

import com.blueberry.model.acc.User;
import com.blueberry.model.app.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUser(User user);

    @Query("SELECT a FROM AppUser a " +
            "WHERE LOWER(CONCAT(a.firstName,' ',a.lastName)) like LOWER(CONCAT('%',:name,'%')) " +
            "AND a.user.isBanned =:banned")
    Iterable<AppUser> findByName(@Param("name") String name,@Param("banned") boolean banned);
}
