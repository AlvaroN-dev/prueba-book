package com.codeup.booknova.ui.service;

import com.codeup.booknova.connection.ConnectionFactory;
import com.codeup.booknova.infra.config.AppConfig;
import com.codeup.booknova.jdbc.JdbcTemplateLight;
import com.codeup.booknova.repository.impl.*;
import com.codeup.booknova.service.impl.*;

/**
 * Service manager that provides centralized access to all business services.
 * This class manages the dependency injection and lifecycle of services.
 */
public class ServiceManager {
    private static ServiceManager instance;
    
    private final JdbcTemplateLight jdbcTemplate;
    private final UserService userService;
    private final BookService bookService;
    private final MemberService memberService;
    private final LoanService loanService;
    private final MembershipRequestService membershipRequestService;
    
    private ServiceManager() {
        // Initialize configuration and connection
        AppConfig config = new AppConfig();
        ConnectionFactory connectionFactory = new ConnectionFactory(config);
        this.jdbcTemplate = new JdbcTemplateLight(connectionFactory);
        
        // Initialize repositories
        UserJdbcRepository userRepo = new UserJdbcRepository(jdbcTemplate);
        BookJdbcRepository bookRepo = new BookJdbcRepository(jdbcTemplate);
        MemberJdbcRepository memberRepo = new MemberJdbcRepository(jdbcTemplate);
        LoanJdbcRepository loanRepo = new LoanJdbcRepository(jdbcTemplate);
        MembershipRequestJdbcRepository requestRepo = new MembershipRequestJdbcRepository(jdbcTemplate);
        
        // Initialize services
        this.userService = new UserService(userRepo);
        this.bookService = new BookService(bookRepo);
        this.memberService = new MemberService(memberRepo);
        this.loanService = new LoanService(loanRepo, bookRepo, memberRepo);
        this.membershipRequestService = new MembershipRequestService(requestRepo, memberRepo);
    }
    
    public static ServiceManager getInstance() {
        if (instance == null) {
            synchronized (ServiceManager.class) {
                if (instance == null) {
                    instance = new ServiceManager();
                }
            }
        }
        return instance;
    }
    
    public UserService getUserService() {
        return userService;
    }
    
    public BookService getBookService() {
        return bookService;
    }
    
    public MemberService getMemberService() {
        return memberService;
    }
    
    public LoanService getLoanService() {
        return loanService;
    }
    
    public MembershipRequestService getMembershipRequestService() {
        return membershipRequestService;
    }
    
    public JdbcTemplateLight getJdbcTemplate() {
        return jdbcTemplate;
    }
}