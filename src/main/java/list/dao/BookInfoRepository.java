package list.dao;

import list.entity.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;


public interface BookInfoRepository extends MongoRepository<BookInfo, String> {

    BookInfo findByBookName(String bookName);

    ArrayList<BookInfo> findByBookId(String bookId);

    Page<BookInfo> findAllByOrderByCreateAtDesc(Pageable pageable);


}
