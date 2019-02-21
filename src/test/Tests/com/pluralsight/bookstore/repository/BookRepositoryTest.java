package com.pluralsight.bookstore.repository;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.NumberGenerator;
import com.pluralsight.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {
    @Inject
    private BookRepository bookRepository;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create( JavaArchive.class )
                .addClass( BookRepository.class )
                .addClass( Book.class )
                .addClass( Language.class )
                .addClass( TextUtil.class )
                .addClass( IsbnGenerator.class )
                .addClass( NumberGenerator.class  )
                .addAsManifestResource( EmptyAsset.INSTANCE, "beans.xml" )
                .addAsManifestResource( "META-INF/test-persistence.xml", "persistence.xml" );


    }
    @Test(expected = Exception.class)
    public void createInvalidBook(){
        Book book = new Book("isbn",null,12F,123,Language.ENGLISH, new Date(),"http://blahblah", "desc" );
        book = bookRepository.create( book );
    }
    @Test(expected = Exception.class)
    public void FindWithInvalidId(){
        bookRepository.find(null);
    }

    @Test
    public void create() {
        assertEquals( Long.valueOf( 0 ),bookRepository.countAll());
        assertEquals( 0,bookRepository.findAll().size()  );

        Book book = new Book("isbn","a title",12F,123,Language.ENGLISH, new Date(),"http://blahblah", "desc" );
        book = bookRepository.create( book );
        Long bookId = book.getId();

        assertNotNull( bookId );

        Book bookFound = bookRepository.find(bookId);

        assertEquals("a title", bookFound.getTitle() );
        assertTrue( bookFound.getIsbn().startsWith( "13" ) );

        assertEquals( Long.valueOf( 1 ),bookRepository.countAll());
        assertEquals( 1,bookRepository.findAll().size()  );

        bookRepository.delete( bookId );
        assertEquals( Long.valueOf( 0 ),bookRepository.countAll());
        assertEquals( 0,bookRepository.findAll().size()  );
    }
}
