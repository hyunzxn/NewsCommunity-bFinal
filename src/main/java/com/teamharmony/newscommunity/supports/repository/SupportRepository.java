package com.teamharmony.newscommunity.supports.repository;


import com.teamharmony.newscommunity.supports.entity.Support;
import com.teamharmony.newscommunity.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportRepository extends JpaRepository<Support, Long> {
    List<Support> findAllByOrderByCreatedAtDesc();

    List<Support> findAllByUsername(String username);

}

