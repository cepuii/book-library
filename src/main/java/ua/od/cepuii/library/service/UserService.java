package ua.od.cepuii.library.service;

import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.sql.SQLException;
import java.util.Optional;

public class UserService {
  
  private final UserRepository userRepository = new JdbcRepositoryFactory().getUserRepository();
  
  public long create(String email, String password, Role role) {
    long insert;
    try {
      insert = userRepository.insert(new User(email, password, role));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return insert;
  }
  
  public User getUserByEmailAndPassword(String email, String password) {
      Optional<User> byEmail = userRepository.getByEmail(email);
      if (byEmail.isPresent() && byEmail.get().getPassword().equals(password)) {
          return byEmail.get();
      }
      return null;
  }
  
  public boolean delete(long id) {
    
    return false;
  }
  
  public boolean block(long id) {
    return false;
  }
  
  public boolean unblock(long id) {
    return false;
  }
  
}
