package com.example.obes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.obes.dao.IBookDAO;
import com.example.obes.model.Book;
import com.example.obes.model.User;

public class UserTest {

    private User commonUser;
    private User institutionalUser;
    @Mock
    private IBookDAO mockBookDAO;
    @Mock
    private Book mockBook;

    @Before
    public void setUp() {
        mockBookDAO = mock(IBookDAO.class);
        commonUser = new User(1, "Common User", "123456789", "user@example.com", "password", true, mockBookDAO);
        institutionalUser = new User(2, "Institutional User", "987654321", "school@example.com", "institutionalPassword", false, mockBookDAO);
    }
    @Test
    public void testDonateABookCommonUser() {
        when(mockBookDAO.addBook(mockBook)).thenReturn(true);

        boolean donationResult = commonUser.donateABook(mockBook);

        verify(mockBookDAO, times(1)).addBook(mockBook);
        assertTrue(donationResult);
    }
    @Test(expected = IllegalStateException.class)
    public void testDonateABookAdminUser() {
        institutionalUser.donateABook(mockBook);
    }
    @Test(expected = IllegalStateException.class)
    public void testDonateABookCommonUserNoDAO() {
        User userWithoutDAO = new User(3, "No DAO User", "987654321", "no.dao@example.com", "password", true, null);
        userWithoutDAO.donateABook(mockBook);
    }
    @Test
    public void testGetIsUserCommon() {
        assertTrue(commonUser.getIsUserCommon());
        assertFalse(institutionalUser.getIsUserCommon());
    }
}