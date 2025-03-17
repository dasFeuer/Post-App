package com.example.barun.repositories;

import com.example.barun.entities.blogEntities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Post, Long> {

}
