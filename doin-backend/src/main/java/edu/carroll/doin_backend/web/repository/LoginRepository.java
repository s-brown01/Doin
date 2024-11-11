package edu.carroll.doin_backend.web.repository;

import java.util.List;
import java.util.Optional;

import edu.carroll.doin_backend.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and managing {@link User} entities in the database.
 * <p>
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom query methods
 * for interacting with the user data in the underlying database. The methods here allow for searching users
 * by their username, both in an exact case-insensitive manner and via partial matching.
 * </p>
 */
public interface LoginRepository extends JpaRepository<User, Integer> {
  /**
   * Finds a list of users by their username, ignoring case.
   * <p>
   * This method is autogenerated by Spring, but it will find all users with the username 
   * that matches the username parameter entered into the method. Assuming usernames have 
   * to be unique, the list should be only 1 User long.
   * </p>
   *
   * @param username the username to search for, case-insensitive.
   * @return a list of {@link User} objects that match the given username, ignoring case.
   */
  List<User> findByUsernameIgnoreCase(String username);

  /**
   * Finds a list of users whose username containing the given username, ignoring case.
   * <p>
   * This method is autogenerated by Spring Data JPA. It finds users where their username
   * contains the provided substring, regardless of case. This allows partial
   * matching of usernames, making it useful for search functionalities where a
   * user might enter part of a username and still find relevant results.
   * </p>
   *
   * @param username the substring to search for in usernames, case-insensitive.
   * @return a list of {@link User} objects whose username contains the given substring,
   *         ignoring case.
   */
  List<User> findByUsernameLikeIgnoreCase(String username);

  /**
   * Finds a user by their username.
   * <p>
   * This method retrieves a {@link User} entity based on the provided username. The search is case-sensitive,
   * meaning the username must exactly match the one stored in the database. The result is wrapped in an
   * {@link Optional}, which will be empty if no user is found with the given username.
   * </p>
   *
   * @param username the username of the user to search for.
   * @return an {@link Optional} containing the {@link User} object if found, or an empty {@link Optional}
   *         if no user exists with the given username.
   */
  Optional<User> findByUsername(String username);

}
