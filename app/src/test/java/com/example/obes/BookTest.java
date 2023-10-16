package com.example.obes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import com.example.obes.model.Book.Book;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookTest {
    @Mock
    private Book mockBook;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testGetters() {
        Book book = new Book(1, "Title", "Description","Category", true, 123,"Author", 19.99, "novo");

        assertEquals(1, book.getId());
        assertEquals("Title", book.getTitle());
        assertEquals("Description", book.getDescription());
        assertEquals("Category", book.getCategory());
        assertTrue(book.isAvailable());
        assertEquals(123, book.getCoverResourceId());
        assertEquals("Author", book.getAuthor());
        assertEquals(19.99, book.getPrice(), 0.001);  // Delta é usado para lidar com a imprecisão de double
    }
    @Test
    public void testSetters() {
        Book book = new Book(1, "title", "description","Category", true, 123,"author", 19.99, "novo");

        book.setTitle("New Title");
        book.setDescription("New Description");
        book.setCategory("New Category");
        book.setAvailable(false);
        book.setCoverResourceId(456);
        book.setPrice(29.99);

        assertEquals("New Title", book.getTitle());
        assertEquals("New Description", book.getDescription());
        assertEquals("New Category", book.getCategory());
        assertFalse(book.isAvailable());
        assertEquals(456, book.getCoverResourceId());
        assertEquals(29.99, book.getPrice(), 0.001);
    }
    @Test
    public void testAvailability() {
        when(mockBook.isAvailable()).thenReturn(true);
        assertTrue(mockBook.isAvailable());

        when(!mockBook.isAvailable()).thenReturn(false);
        assertFalse(mockBook.isAvailable());
    }
}

