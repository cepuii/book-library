package ua.od.cepuii.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.od.cepuii.library.dto.FilterParams;
import ua.od.cepuii.library.dto.Page;
import ua.od.cepuii.library.entity.Book;
import ua.od.cepuii.library.repository.BookRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.od.cepuii.library.util.BookUtil.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insert() {
        when(bookRepository.insert(NEW_BOOK)).thenReturn(CREATE_BOOK_ID);
        assertDoesNotThrow(() -> bookService.createOrUpdate(NEW_BOOK));
        verify(bookRepository, times(1)).insert(NEW_BOOK);
        verify(bookRepository, times(0)).update(NEW_BOOK);
    }

    @Test
    void insertEmptyBook() {
        when(bookRepository.insert(EMPTY_BOOK)).thenReturn(CREATE_BOOK_ID);
        assertFalse(() -> bookService.createOrUpdate(EMPTY_BOOK).hasErrors());
        verify(bookRepository, times(1)).insert(EMPTY_BOOK);
        verify(bookRepository, times(0)).update(any(Book.class));
    }

    @Test
    void getById() {
        when(bookRepository.getById(LOAN_ID)).thenReturn(Optional.ofNullable(BOOK));
        assertEquals(BOOK_TO, bookService.getById(LOAN_ID));
    }

    @Test
    void getByIdNotFound() {
        when(bookRepository.getById(NOT_FOUND_ID)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bookService.getById(LOAN_ID));
    }

    @Test
    void update() {
        when(bookRepository.update(BOOK)).thenReturn(true);
        assertFalse(() -> bookService.createOrUpdate(BOOK).hasErrors());
        verify(bookRepository, times(0)).insert(any(Book.class));
        verify(bookRepository, times(1)).update(BOOK);
    }

    @Test
    void updateNotExistBook() {
        when(bookRepository.update(BOOK_NOT_EXIST)).thenReturn(false);
        assertTrue(() -> bookService.createOrUpdate(BOOK_NOT_EXIST).hasErrors());
        verify(bookRepository, times(0)).insert(any(Book.class));
        verify(bookRepository, times(1)).update(BOOK_NOT_EXIST);
    }

    @Test
    void delete() {
        when(bookRepository.delete(LOAN_ID)).thenReturn(true);
        assertFalse(() -> bookService.delete(LOAN_ID).hasErrors());
        verify(bookRepository, times(1)).delete(anyLong());
    }

    @Test
    void getAll() {
        Page page = mock(Page.class);
        FilterParams filter = mock(FilterParams.class);
        when(filter.getOrderBy()).thenReturn("b_title");
        when(filter.isDescending()).thenReturn(false);
        when(page.getLimit()).thenReturn(5);
        when(page.getOffset()).thenReturn(0);
        when(bookRepository.getAll(filter, "b_title", page.getLimit(), page.getOffset())).thenReturn(List.of(BOOK));
        assertIterableEquals(List.of(BOOK_TO), bookService.getAll(page, filter));
        verify(bookRepository, times(1)).getAll(any(FilterParams.class), anyString(), anyInt(), anyInt());
    }

    @Test
    void getPageAmount() {
        Page page = mock(Page.class);
        when(page.getNoOfRecords()).thenReturn(NO_OF_RECORDS);
        FilterParams filter = mock(FilterParams.class);
        when(bookRepository.getCount(filter)).thenReturn(3);
        assertEquals(1, bookService.getPageAmount(page, filter));
    }

}