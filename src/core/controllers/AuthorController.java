/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Audiobook;
import core.models.Author;
import core.models.Book;
import core.models.DigitalBook;
import core.models.PrintedBook;
import core.models.repositories.IPersonRepository;
import core.models.repositories.IRepositoryProvider;
import core.models.repositories.RepositoryProvider;
import java.util.ArrayList;

/**
 *
 * @author famil
 */
public class AuthorController {

    private static IRepositoryProvider repositoryProvider = RepositoryProvider.getProvider();
    private static IPersonRepository personRepository = repositoryProvider.getPersonRepository();

    public static void setRepositoryProvider(IRepositoryProvider customProvider) {
        if (customProvider != null) {
            repositoryProvider = customProvider;
            personRepository = customProvider.getPersonRepository();
        }
    }

    public static Response createAuthor(String id, String firstName, String lastName) {
        try {

            if (id == null || id.trim().isEmpty()) {
                return new Response("El ID no puede estar vacío.", Status.BAD_REQUEST);
            }

            if (id.trim().length() > 15) {
                return new Response("El ID debe tener máximo 15 dígitos.", Status.BAD_REQUEST);
            }

            long idLong;
            try {
                idLong = Long.parseLong(id.trim());

                if (idLong < 0) {
                    return new Response("El ID debe ser positivo.", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException ex) {
                return new Response("El ID debe ser numérico.", Status.BAD_REQUEST);
            }

            if (firstName == null || firstName.trim().isEmpty()) {
                return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
            }

            if (lastName == null || lastName.trim().isEmpty()) {
                return new Response("El apellido no puede estar vacío.", Status.BAD_REQUEST);
            }

            if (personRepository.findById(idLong) != null) {
                return new Response("Otra persona ya tiene este ID.", Status.BAD_REQUEST);
            }

            Author author = new Author(idLong, firstName, lastName);
            boolean added = personRepository.add(author);

            if (!added) {
                return new Response("No se pudo guardar el autor.", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Autor creado correctamente.", Status.CREATED, author.clone());

        } catch (Exception ex) {
            return new Response("Error inesperado.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getBooksByAuthor(String comboItem) {
        try {

            if (comboItem == null || comboItem.trim().isEmpty() || comboItem.startsWith("Seleccione")) {
                return new Response("Debe seleccionar un autor válido.", Status.BAD_REQUEST);
            }

            String[] data = comboItem.split(" - ");
            if (data.length == 0) {
                return new Response("Formato inválido.", Status.BAD_REQUEST);
            }

            long authorId;
            try {
                authorId = Long.parseLong(data[0].trim());
            } catch (Exception e) {
                return new Response("ID inválido.", Status.BAD_REQUEST);
            }

            Author author = (Author) personRepository.findById(authorId);
            if (author == null) {
                return new Response("El autor no existe.", Status.NOT_FOUND);
            }

            ArrayList<Book> books = new ArrayList<>();
            try {
                for (Book book : author.getBooks()) {
                    books.add(book.clone());
                }
            } catch (CloneNotSupportedException cloneException) {
                return new Response("Error al copiar libros del autor.", Status.INTERNAL_SERVER_ERROR);
            }

            if (books.isEmpty()) {
                return new Response("Este autor no tiene libros registrados.", Status.NOT_FOUND);
            }

            for (int i = 0; i < books.size() - 1; i++) {
                for (int j = i + 1; j < books.size(); j++) {
                    if (books.get(i).getIsbn().compareTo(books.get(j).getIsbn()) > 0) {
                        Book temp = books.get(i);
                        books.set(i, books.get(j));
                        books.set(j, temp);
                    }
                }
            }

            ArrayList<Object[]> rows = new ArrayList<>();

            for (Book book : books) {

                String authorsNames = "";
                if (!book.getAuthors().isEmpty()) {
                    authorsNames = book.getAuthors().get(0).getFullname();
                    for (int i = 1; i < book.getAuthors().size(); i++) {
                        authorsNames += ", " + book.getAuthors().get(i).getFullname();
                    }
                }

                if (book instanceof PrintedBook printed) {
                    rows.add(new Object[]{
                        printed.getTitle(), authorsNames, printed.getIsbn(),
                        printed.getGenre(), printed.getFormat(), printed.getValue(),
                        printed.getPublisher().getName(), printed.getCopies(),
                        printed.getPages(), "-", "-", "-"
                    });

                } else if (book instanceof DigitalBook digital) {
                    rows.add(new Object[]{
                        digital.getTitle(), authorsNames, digital.getIsbn(),
                        digital.getGenre(), digital.getFormat(), digital.getValue(),
                        digital.getPublisher().getName(), "-", "-",
                        digital.hasHyperlink() ? digital.getHyperlink() : "No", "-", "-"
                    });

                } else if (book instanceof Audiobook audio) {
                    rows.add(new Object[]{
                        audio.getTitle(), authorsNames, audio.getIsbn(),
                        audio.getGenre(), audio.getFormat(), audio.getValue(),
                        audio.getPublisher().getName(), "-", "-", "-",
                        audio.getNarrador().getFullname(), audio.getDuration()
                    });
                }
            }

            return new Response("Libros obtenidos correctamente.", Status.OK, rows);

        } catch (Exception e) {
            return new Response("Error inesperado.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getAuthorsWithMaxPublishers() {
        try {
            ArrayList<Author> authors = personRepository.getAuthors();

            if (authors == null || authors.isEmpty()) {
                return new Response("No hay autores registrados.", Status.NOT_FOUND);
            }

            int maxPublishers = -1;
            for (Author a : authors) {
                if (a.getPublisherQuantity() > maxPublishers) {
                    maxPublishers = a.getPublisherQuantity();
                }
            }

            ArrayList<Author> authorsMax = new ArrayList<>();
            try {
                for (Author a : authors) {
                    if (a.getPublisherQuantity() == maxPublishers) {
                        authorsMax.add(a.clone());
                    }
                }
            } catch (CloneNotSupportedException cloneException) {
                return new Response("Error al copiar autores.", Status.INTERNAL_SERVER_ERROR);
            }

            authorsMax.sort((a1, a2) -> Long.compare(a1.getId(), a2.getId()));

            ArrayList<Object[]> rows = new ArrayList<>();
            for (Author a : authorsMax) {
                rows.add(new Object[]{
                    a.getId(),
                    a.getFullname(),
                    a.getPublisherQuantity()
                });
            }

            return new Response("Autores obtenidos correctamente.", Status.OK, rows);

        } catch (Exception e) {
            return new Response("Error inesperado al consultar autores.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
