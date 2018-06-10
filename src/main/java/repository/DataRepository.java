package repository;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Data;


@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
	Page<Data> findByFileId(Long DataFileId, Pageable pageable);
}
