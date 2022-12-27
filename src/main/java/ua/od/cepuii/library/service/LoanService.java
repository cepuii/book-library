package ua.od.cepuii.library.service;

import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.RepositoryFactory;
import ua.od.cepuii.library.repository.jdbc.JdbcRepositoryFactory;

import java.util.Collection;

public class LoanService {


    RepositoryFactory repositoryFactory = new JdbcRepositoryFactory();

    public long create(Loan loan) {

        return 0;
    }

    public boolean update(Loan loan) {

        return false;
    }

    boolean delete(long id) {

        return false;
    }

    public Collection<Loan> getByStatusRaw() {
        return null;
    }

    public Collection<Loan> getAll(int currentPage) {
        return null;
    }

    public Collection<Loan> getByReader(long userId) {
        return null;
    }
}
