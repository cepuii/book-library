package ua.od.cepuii.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.Loan;
import ua.od.cepuii.library.repository.LoanRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.od.cepuii.library.util.LoanUtil.*;

class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        when(loanRepository.insert(NEW_LOAN)).thenReturn(LOAN_ID);
        assertEquals(LOAN_ID, loanService.create(NEW_LOAN));
        verify(loanRepository, times(1)).insert(NEW_LOAN);
    }

    @Test
    void delete() {
        when(loanRepository.delete(LOAN_ID)).thenReturn(true);
        assertTrue(() -> loanService.delete(LOAN_ID));
        verify(loanRepository, times(1)).delete(LOAN_ID);
    }

    @Test
    void getAll() {
        Page page = mock(Page.class);
        FilterParams filter = mock(FilterParams.class);
        when(filter.getOrderBy()).thenReturn("");
        when(filter.isDescending()).thenReturn(false);
        when(page.getLimit()).thenReturn(5);
        when(page.getOffset()).thenReturn(0);
        when(loanRepository.getAll(filter, filter.getOrderBy(), page.getLimit(), page.getOffset())).thenReturn(List.of(LOAN));
        assertIterableEquals(List.of(LOAN_TO), loanService.getAll(filter, page));
        verify(loanRepository, times(1)).getAll(any(FilterParams.class), anyString(), anyInt(), anyInt());
    }

    @Test
    void getAllByUserId() {
        Page page = mock(Page.class);
        when(page.getLimit()).thenReturn(5);
        when(page.getOffset()).thenReturn(0);
        when(loanRepository.getAllByUserId(USER_ID, page.getLimit(), page.getOffset())).thenReturn(List.of(LOAN));
        assertIterableEquals(List.of(LOAN_TO), loanService.getAllByUserId(USER_ID, page));
        verify(loanRepository, times(1)).getAllByUserId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void setOrderStatus() {
        when(loanRepository.updateStatus(LOAN, false)).thenReturn(true);
        assertTrue(() -> loanService.setOrderStatus(LOAN, false));
        verify(loanRepository, times(1)).updateStatus(any(Loan.class), anyBoolean());
    }

    @Test
    void getBooksIdsByUserId() {
        when(loanRepository.getBooksIdsByUserId(USER_ID)).thenReturn(List.of(BOOK_ID));
        assertIterableEquals(List.of(BOOK_ID), loanService.getBooksIdsByUserId(USER_ID));
        verify(loanRepository, times(1)).getBooksIdsByUserId(anyLong());
    }

    @Test
    void getLoanHistory() {
        Page page = mock(Page.class);
        when(page.getLimit()).thenReturn(LIMIT);
        when(page.getOffset()).thenReturn(OFFSET);
        when(loanRepository.getLoanHistory(USER_ID, LIMIT, OFFSET)).thenReturn(List.of(LOAN));
        assertIterableEquals(List.of(LOAN_TO), loanService.getLoanHistory(USER_ID, page));
        verify(loanRepository, times(1)).getLoanHistory(anyLong(), anyInt(), anyInt());
        verify(page, times(1)).getLimit();
        verify(page, times(1)).getOffset();
    }

    @Test
    void updateFine() {
        assertDoesNotThrow(() -> loanService.updateFine());
    }
}