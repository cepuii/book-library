package ua.od.cepuii.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.od.cepuii.library.dto.FilterAndSortParams;
import ua.od.cepuii.library.dto.Mapper;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.dto.UserTO;
import ua.od.cepuii.library.entity.User;
import ua.od.cepuii.library.entity.enums.Role;
import ua.od.cepuii.library.repository.UserRepository;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class UserService implements Service {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
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

    public int getPageAmount(Page page, FilterAndSortParams filterParam) {
        int recordsAmount = userRepository.getCount(filterParam);
        log.info("get records amount: {}", recordsAmount);
        int pageAmount = (recordsAmount % page.getNoOfRecords()) == 0 ? (recordsAmount / page.getNoOfRecords()) : (1 + (recordsAmount / page.getNoOfRecords()));
        log.info("get page amount: {}", pageAmount);
        return pageAmount;
    }

    public Collection<UserTO> getAll(Page currentPage, FilterAndSortParams filterParam) {
        String orderBy = (filterParam.getOrderBy().isBlank() ? "email" : filterParam.getOrderBy()) + (filterParam.isDescending() ? " DESC" : "");
        int limit = currentPage.getNoOfRecords();
        int offset = currentPage.getNoOfRecords() * (currentPage.getCurrentPage() - 1);
        log.info(String.format("getAll books:filter %s ; %s order %s, descending %b, limit %d , offset %d",
                filterParam.getFirstParam(), filterParam.getSecondParam(), filterParam.getOrderBy(), filterParam.isDescending(), limit, offset));
        Collection<User> users = userRepository.getAll(filterParam, orderBy, limit, offset);
        return Mapper.mapToUserTO(users);
    }
}
