package com.pluralsight.bookstore.rest;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import com.pluralsight.bookstore.repository.BookRepository;
import com.pluralsight.bookstore.util.IsbnGenerator;
import com.pluralsight.bookstore.util.NumberGenerator;
import com.pluralsight.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Date;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class BookEndpointTest {
    private Response response;

    @Deployment(testable = false)
    public static Archive createDeploymentPackage() {

        return ShrinkWrap.create( WebArchive.class )
                .addClass( Book.class )
                .addClass( Language.class )
                .addClass( BookRepository.class )
                .addClass( NumberGenerator.class )
                .addClass( IsbnGenerator.class )
                .addClass( TextUtil.class )
                .addClass( BookEndpoint.class )
                .addClass( JAXRSConfiguration.class )
                .addAsWebInfResource( EmptyAsset.INSTANCE, "beans.xml" )
                .addAsResource( "META-INF/test-persistence.xml", "META-INF/persistence.xml" );
    }

    @Test
    @InSequence(2)
    public void createBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {

        //Test counting books
        response = webTarget.path( "count" ).request().get();
        assertEquals( NO_CONTENT.getStatusCode(), response.getStatus() );

        response = webTarget.request( APPLICATION_JSON ).get();
        assertEquals( NO_CONTENT.getStatusCode(), response.getStatus() );

    }

    @Test
    @InSequence(3)
    public void shouldCreateABook(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        //create book
        Book book = new Book( "isbn", "a tiiiite", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description" );
        response = webTarget.request( APPLICATION_JSON ).post( Entity.entity( book, APPLICATION_JSON ) );
        assertEquals( CREATED.getStatusCode(), response.getStatus() );
    }

    @Test
    @InSequence(4)
    public void shouldCreateABook1(@ArquillianResteasyResource("api/books") WebTarget webTarget) {
        // Creates a book
        Book book = new Book( "isbn", "a title", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description" );
        response = webTarget.request( APPLICATION_JSON ).post( Entity.entity( book, APPLICATION_JSON ) );
        assertEquals( CREATED.getStatusCode(), response.getStatus() );

    }
}

