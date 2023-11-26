package com.synrgy.commit.repository;

import com.synrgy.commit.model.FilePost;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FilePostRepository extends PagingAndSortingRepository<FilePost, Long> {
}
