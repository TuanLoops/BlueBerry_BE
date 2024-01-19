package com.blueberry.repository;

import com.blueberry.model.app.AppUser;
import com.blueberry.model.app.PrivacyLevel;
import com.blueberry.model.app.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByIdAndIsDeleted(Long id,boolean deleted);
    @Query("SELECT s FROM Status s " +
            "    WHERE ((s.author = :author AND s.privacyLevel != 'PRIVATE' )OR (s.author IN :friendList AND s.privacyLevel = 'FRIENDS')" +
            "       OR s.privacyLevel='PUBLIC' ) AND s.isDeleted = false ORDER BY s.lastActivity DESC")
    Iterable<Status> findAllByPrivacy(@Param("author") AppUser author, @Param("friendList") List<AppUser> friendList);

    @Query("SELECT s FROM Status s " +
            "    WHERE ((s.author = :author AND s.privacyLevel != 'PRIVATE' )OR (s.author IN :friendList AND s.privacyLevel = 'FRIENDS')" +
            "       OR s.privacyLevel='PUBLIC' ) AND s.isDeleted = false" +
            "       AND LOWER(s.body) LIKE LOWER(concat('%',:body,'%'))  ORDER BY s.lastActivity DESC ")
    Iterable<Status> findStatusByNameContaining(@Param("author") AppUser author, @Param("friendList") List<AppUser> friendList,@Param("body") String body);

    @Query("SELECT s FROM Status s " +
            "    WHERE s.author = :author AND s.privacyLevel IN :privacy AND s.isDeleted = false " +
            "    ORDER BY s.lastActivity DESC")
    Iterable<Status> findAllByAuthor(@Param("author") AppUser author, @Param("privacy") List<PrivacyLevel> privacyLevels);
}
