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
import core.models.Narrator;
import core.models.PrintedBook;
import core.models.Publisher;
import core.models.repositories.IBookRepository;
import core.models.repositories.IPersonRepository;
import core.models.repositories.IPublisherRepository;
import core.models.repositories.IRepositoryProvider;
import core.models.repositories.RepositoryProvider;
import java.util.ArrayList;

/**
 *
 * @author famil
 */
public class BookController {
    
    private static IRepositoryProvider repositoryProvider = RepositoryProvider.getProvider();
    private static IBookRepository bookRepository = repositoryProvider.getBookRepository();
    private static IPersonRepository personRepository = repositoryProvider.getPersonRepository();
    private static IPublisherRepository publisherRepository = repositoryProvider.getPublisherRepository();

    public static void setRepositoryProvider(IRepositoryProvider customProvider) {
        if (customProvider != null) {
            repositoryProvider = customProvider;
            bookRepository = customProvider.getBookRepository();
            personRepository = customProvider.getPersonRepository();
            publisherRepository = customProvider.getPublisherRepository();
        }
    }
    
 public static Response createBook(
            String title, String authorsV, String isbn, String genre, String format, String valueV, String publisherV,
            boolean printed, boolean digital, boolean audio, String hyperlink, String durationV, String narratorV,
            String pagesV, String copiesV) {

        try {
            
            if (title.trim().isEmpty()) {
                return new Response("El título no puede estar vacío.", Status.BAD_REQUEST);
            }

            if (authorsV.trim().isEmpty()) {
                return new Response("Debe ingresar al menos un autor.", Status.BAD_REQUEST);
            }

            isbn = isbn.trim();
            if (isbn.isEmpty()) {
                return new Response("El ISBN no puede estar vacío.", Status.BAD_REQUEST);
            }

            if (!isbn.matches("\\d{3}-\\d-\\d{2}-\\d{6}-\\d")) {
                return new Response("ISBN inválido. Formato requerido: XXX-X-XX-XXXXXX-X", Status.BAD_REQUEST);
            }

            if (bookRepository.findByIsbn(isbn) != null) {
                return new Response("Este ISBN ya está registrado.", Status.BAD_REQUEST);
            }

            if (genre.equals("Seleccione uno...")) {
                return new Response("Debe seleccionar un género.", Status.BAD_REQUEST);
            }

            if (format.equals("Seleccione uno...")) {
                return new Response("Debe seleccionar un formato.", Status.BAD_REQUEST);
            }

            if (!printed && !digital && !audio) {
                return new Response("Debe seleccionar un tipo de libro.", Status.BAD_REQUEST);
            }

            
            double value;
            try {
                value = Double.parseDouble(valueV.trim());
                if (value <= 0) {
                    return new Response("El valor debe ser mayor que 0.", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("El valor debe ser numérico.", Status.BAD_REQUEST);
            }

            
            if (publisherV.equals("Seleccione uno...")) {
                return new Response("Debe seleccionar una editorial.", Status.BAD_REQUEST);
            }

            int i = publisherV.indexOf("(");
            int j = publisherV.indexOf(")");
            String nit = publisherV.substring(i + 1, j).trim();

            Publisher publisher = publisherRepository.findByNit(nit);

            if (publisher == null) {
                return new Response("La editorial no existe.", Status.BAD_REQUEST);
            }

            
            String[] lines = authorsV.split("\n");
            ArrayList<Author> authors = new ArrayList<>();
            ArrayList<Long> idsRepetidos = new ArrayList<>();

            for (String line : lines) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                long id = Long.parseLong(line.split(" - ")[0].trim());

                
                for (Long usado : idsRepetidos) {
                    if (usado == id) {
                        return new Response("No puede repetir autores en el mismo libro.", Status.BAD_REQUEST);
                    }
                }

                idsRepetidos.add(id);

                Author a = (Author) personRepository.findById(id);
                if (a == null) {
                    return new Response("El autor con ID " + id + " no existe.", Status.BAD_REQUEST);
                }

                authors.add(a);
            }

            if (authors.isEmpty()) {
                return new Response("Debe seleccionar al menos un autor válido.", Status.BAD_REQUEST);
            }

           
            Book created = null;

            
            if (printed) {

                if (pagesV.trim().isEmpty()) {
                    return new Response("Debe ingresar número de páginas.", Status.BAD_REQUEST);
                }

                if (copiesV.trim().isEmpty()) {
                    return new Response("Debe ingresar número de ejemplares.", Status.BAD_REQUEST);
                }

                int pages, copies;
                try {
                    pages = Integer.parseInt(pagesV.trim());
                    copies = Integer.parseInt(copiesV.trim());

                    if (pages <= 0) {
                        return new Response("Las páginas deben ser mayores a 0.", Status.BAD_REQUEST);
                    }

                    if (copies <= 0) {
                        return new Response("Las copias deben ser mayores a 0.", Status.BAD_REQUEST);
                    }

                } catch (Exception e) {
                    return new Response("Páginas o copias inválidas.", Status.BAD_REQUEST);
                }

                created = new PrintedBook(title, authors, isbn, genre, format, value, publisher, pages, copies);
            } 
            else if (digital) {

                if (hyperlink.trim().isEmpty()) {
                    created = new DigitalBook(title, authors, isbn, genre, format, value, publisher);
                } else {
                    created = new DigitalBook(title, authors, isbn, genre, format, value, publisher, hyperlink);
                }

            } 
            else if (audio) {

                if (narratorV.equals("Seleccione uno...")) {
                    return new Response("Debe seleccionar un narrador.", Status.BAD_REQUEST);
                }

                long nId = Long.parseLong(narratorV.split(" - ")[0].trim());

                Narrator narrator = (Narrator) personRepository.findById(nId);

                if (narrator == null) {
                    return new Response("El narrador no existe.", Status.BAD_REQUEST);
                }

                if (durationV.trim().isEmpty()) {
                    return new Response("La duración no puede estar vacía.", Status.BAD_REQUEST);
                }

                int duration;
                try {
                    duration = Integer.parseInt(durationV.trim());
                    if (duration <= 0) {
                        return new Response("La duración debe ser mayor a 0.", Status.BAD_REQUEST);
                    }

                } catch (Exception e) {
                    return new Response("Duración inválida.", Status.BAD_REQUEST);
                }

                created = new Audiobook(title, authors, isbn, genre, format, value, publisher, duration, narrator);
            }

            
            boolean ok = bookRepository.add(created);

            if (!ok) {
                return new Response("Error al guardar el libro.", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Libro creado correctamente.", Status.CREATED, created.clone());

        } catch (Exception e) {
            return new Response("Error inesperado al crear libro.", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public static Response listBooksByType(String filter) {
        try {
            if (filter == null || filter.trim().isEmpty()) {
                return new Response("Debe seleccionar un tipo de libro.", Status.BAD_REQUEST);
            }

            ArrayList<Book> books = new ArrayList<>(bookRepository.getAll());
            ArrayList<Book> filtered = new ArrayList<>();

            switch (filter) {
                case "Libros Impresos" -> {
                    for (Book b : books) {
                        if (b instanceof PrintedBook) {
                            filtered.add(b);
                        }
                    }
                }
                case "Libros Digitales" -> {
                    for (Book b : books) {
                        if (b instanceof DigitalBook) {
                            filtered.add(b);
                        }
                    }
                }
                case "Audiolibros" -> {
                    for (Book b : books) {
                        if (b instanceof Audiobook) {
                            filtered.add(b);
                        }
                    }
                }
                case "Todos los Libros" ->
                    filtered.addAll(books);
                default -> {
                    return new Response("Filtro de búsqueda inválido.", Status.BAD_REQUEST);
                }
            }

            if (filtered.isEmpty()) {
                return new Response("No hay libros para mostrar.", Status.NOT_FOUND);
            }

            filtered.sort((a, b) -> a.getIsbn().compareTo(b.getIsbn()));

            ArrayList<Book> bookCopies = new ArrayList<>();
            try {
                for (Book book : filtered) {
                    bookCopies.add(book.clone());
                }
            } catch (CloneNotSupportedException cloneException) {
                return new Response("Error al copiar libros.", Status.INTERNAL_SERVER_ERROR);
            }

            ArrayList<Object[]> rows = mapBooksToRows(bookCopies);

            return new Response("Libros obtenidos correctamente.", Status.OK, rows);

        } catch (Exception e) {
            return new Response("Error inesperado al consultar libros.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    
    private static ArrayList<Object[]> mapBooksToRows(ArrayList<Book> books) {
        ArrayList<Object[]> rows = new ArrayList<>();

        for (Book book : books) {

            String authors = "";
            if (!book.getAuthors().isEmpty()) {
                authors = book.getAuthors().get(0).getFullname();
                for (int i = 1; i < book.getAuthors().size(); i++) {
                    authors += ", " + book.getAuthors().get(i).getFullname();
                }
            }

            Object[] row;

            if (book instanceof PrintedBook printed) {
                row = new Object[]{
                    printed.getTitle(), authors, printed.getIsbn(), printed.getGenre(),
                    printed.getFormat(), printed.getValue(), printed.getPublisher().getName(),
                    printed.getCopies(), printed.getPages(), "-", "-", "-"
                };

            } else if (book instanceof DigitalBook digital) {
                row = new Object[]{
                    digital.getTitle(), authors, digital.getIsbn(), digital.getGenre(),
                    digital.getFormat(), digital.getValue(), digital.getPublisher().getName(),
                    "-", "-", digital.hasHyperlink() ? digital.getHyperlink() : "No",
                    "-", "-"
                };

            } else { // Audiobook
                Audiobook audio = (Audiobook) book;
                row = new Object[]{
                    audio.getTitle(), authors, audio.getIsbn(), audio.getGenre(),
                    audio.getFormat(), audio.getValue(), audio.getPublisher().getName(),
                    "-", "-", "-", audio.getNarrador().getFullname(), audio.getDuration()
                };
            }

            
            boolean exists = false;
            for (Object[] r : rows) {
                if (r[2].equals(book.getIsbn())) { 
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                rows.add(row);
            }
        }

        return rows;
    }

    
    public static Response getBooksByFormat(String format) {
        try {
            if (format == null || format.trim().isEmpty()) {
                return new Response("Debe seleccionar un formato.", Status.BAD_REQUEST);
            }

            String fmt = format.trim();

            ArrayList<Book> filteredBooks = new ArrayList<>();

            for (Book book : bookRepository.getAll()) {
                if (book.getFormat().equalsIgnoreCase(fmt)) {
                    filteredBooks.add(book);
                }
            }

            if (filteredBooks.isEmpty()) {
                return new Response("No hay libros con ese formato.", Status.NOT_FOUND);
            }

            
            filteredBooks.sort((a, b) -> a.getIsbn().compareTo(b.getIsbn()));

            ArrayList<Book> bookCopies = new ArrayList<>();
            try {
                for (Book book : filteredBooks) {
                    bookCopies.add(book.clone());
                }
            } catch (CloneNotSupportedException cloneException) {
                return new Response("Error al copiar libros.", Status.INTERNAL_SERVER_ERROR);
            }

            ArrayList<Object[]> rows = mapBooksToRows(bookCopies);

            return new Response("Libros obtenidos correctamente.", Status.OK, rows);

        } catch (Exception e) {
            return new Response("Error inesperado al consultar libros por formato.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
