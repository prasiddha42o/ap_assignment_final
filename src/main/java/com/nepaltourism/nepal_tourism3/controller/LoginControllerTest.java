package com.nepaltourism.nepal_tourism3.controller;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class LoginControllerTest {
    @Test
    public void testValidLogin() {
        LoginController controller = new LoginController();
        assertTrue(controller.isValidLogin("testuser", "testpass"));
        System.out.println("testValidLogin: PASSED");
    }

    @Test
    public void testInvalidLogin() {
        LoginController controller = new LoginController();
        assertFalse(controller.isValidLogin("wronguser", "wrongpass"));
        System.out.println("testInvalidLogin: PASSED");
    }

    @Test
    public void testEmptyUsername() {
        LoginController controller = new LoginController();
        assertFalse(controller.isValidLogin("", "password"));
        System.out.println("testEmptyUsername: PASSED");
    }

    @Test
    public void testEmptyPassword() {
        LoginController controller = new LoginController();
        assertFalse(controller.isValidLogin("user", ""));
        System.out.println("testEmptyPassword: PASSED");
    }
}