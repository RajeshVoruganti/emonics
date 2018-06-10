package repository;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import model.Data;
import model.DataFile;
import java.util.Date;
import java.util.List;


@Repository
public interface DataFileRepository extends JpaRepository<DataFile, Long>{
	 @Query("select d from DataFile d where d.createdAt > ?1")
	 List<DataFile> findByTime(Date emailAddress);

}
