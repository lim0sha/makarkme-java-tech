package interfaces;

import entities.User;
import entities.enums.HairColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);

    @Query("SELECT u FROM User u WHERE (:hairColor IS NULL OR u.hairColor = :hairColor) AND (:gender IS NULL OR u.gender = :gender)")
    List<User> findFilteredUsers(@Param("hairColor") HairColor hairColor, @Param("gender") String gender);
}