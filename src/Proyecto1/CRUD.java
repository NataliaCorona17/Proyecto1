/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proyecto1;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

class CRUD {
    private ArrayList<Book> books;
    private ArrayList<Client> clients;
    private ArrayList<Author> authors;
    private ArrayList<Transaction> transactions;

    public CRUD() {
        this.books = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }
     
    void createBook(Book book) {
        if (book.getAuthor() != null && authors.contains(book.getAuthor())) {
            books.add(book);
            book.getAuthor().getBooks().add(book);
        } else {
            System.out.println("El autor no existe.");
        }
    }

    Book readBook(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }

    void updateBook(Book book) {
        for (Book b : books) {
            if (b.getIsbn().equals(book.getIsbn())) {
                b.setTitle(book.getTitle());
                b.setAuthor(book.getAuthor());
                b.setPublishDate(book.getPublishDate());
                b.setIsAvailable(book.getIsAvailable());
            }
        }
    }

    void deleteBook(String isbn) {
        Book bookToDelete = null;
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                if (!book.getIsAvailable()) {
                    System.out.println("El libro está en poder de un cliente y no puede ser eliminado.");
                    return;
                }
                bookToDelete = book;
                break;
            }
        }
        if (bookToDelete != null) {
            books.remove(bookToDelete);
            bookToDelete.getAuthor().getBooks().remove(bookToDelete);
        }
    }
      
    void createClient(Client client) {
        clients.add(client);
    }

    Client readClient(String clientId) {
        for (Client client : clients) {
            if (client.getProfile().getName().equals(clientId)) {
                return client;
            }
        }
        return null;
    }

    void updateClient(Client client) {
        for (Client c : clients) {
            if (c.getProfile().getName().equals(client.getProfile().getName())) {
                c.setProfile(client.getProfile());
                c.setBorrowedBooks(client.getBorrowedBooks());
            }
        }
    }

    void deleteClient(String clientId) {
        Client clientToDelete = null;
        for (Client client : clients) {
            if (client.getProfile().getName().equals(clientId)) {
                if (!client.getBorrowedBooks().isEmpty()) {
                    System.out.println("El cliente tiene libros en su poder y no puede ser eliminado.");
                    return;
                }
                clientToDelete = client;
                break;
            }
        }
        if (clientToDelete != null) {
            clients.remove(clientToDelete);
        }
    }
   
    void createAuthor(Author author) {
        authors.add(author);
    }

    Author readAuthor(String authorId) {
        for (Author author : authors) {
            if (author.getProfile().getName().equals(authorId)) {
                return author;
            }
        }
        return null;
    }

    void updateAuthor(Author author) {
        for (Author a : authors) {
            if (a.getProfile().getName().equals(author.getProfile().getName())) {
                a.setProfile(author.getProfile());
                a.setBooks(author.getBooks());
            }
        }
    }

    void deleteAuthor(String authorId) {
        Author authorToDelete = null;
        for (Author author : authors) {
            if (author.getProfile().getName().equals(authorId)) {
                if (!author.getBooks().isEmpty()) {
                    System.out.println("El autor tiene libros escritos y no puede ser eliminado.");
                    return;
                }
                authorToDelete = author;
                break;
            }
        }
        if (authorToDelete != null) {
            authors.remove(authorToDelete);
        }
    }
  
          void borrowBook(Client client, Book book) {
    if (client.getBorrowedBooks().size() >= 3) {
        System.out.println("El cliente ya tiene 3 libros prestados y no puede pedir prestado más.");
        return;
    }
    if (!book.getIsAvailable()) {
        System.out.println("El libro no está disponible para préstamo.");
        return;
    }
    book.setIsAvailable(false);
    client.getBorrowedBooks().add(book);
    
    
    Date currentDate = new Date();  
    
    
    Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Borrow", client, book, currentDate);
    transactions.add(transaction);
}


    void returnBook(Client client, Book book) {
    if (!client.getBorrowedBooks().contains(book)) {
        System.out.println("El cliente no tiene este libro en su poder.");
        return;
    }
    book.setIsAvailable(true);
    client.getBorrowedBooks().remove(book);
    
    
    Date currentDate = new Date();  
    
   
    Transaction transaction = new Transaction(UUID.randomUUID().toString(), "Return", client, book, currentDate);
    transactions.add(transaction);
}

    

    
    ArrayList<Transaction> generateBookReport(Book book) {
        ArrayList<Transaction> report = new ArrayList<>();

     
        if (book == null) {
            System.out.println("Libro no especificado.");
            return report;
        }

     
        Date publishDate = book.getPublishDate();
        if (publishDate == null) {
            System.out.println("Fecha de publicación del libro no disponible.");
            return report;
        }

        
        Date currentDate = new Date();

     
        for (Transaction transaction : transactions) {
            if (transaction.getBook().getIsbn().equals(book.getIsbn())
                    && transaction.getDate().after(publishDate)
                    && transaction.getDate().before(currentDate)) {
                report.add(transaction);
            }
        }

        return report;
    }

    ArrayList<Transaction> generateReportByClient(Client client) {
        ArrayList<Transaction> report = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getClient() != null && transaction.getClient().equals(client)) {
                report.add(transaction);
            }
        }
        return report;
    }

   
    ArrayList<Transaction> generateReportByDate(Date startDate, Date endDate) {
        ArrayList<Transaction> report = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Date transactionDate = transaction.getDate();
            if (transactionDate != null && !transactionDate.before(startDate) && !transactionDate.after(endDate)) {
                report.add(transaction);
            }
        }
        return report;
    }
void displayReport(ArrayList<Transaction> report) {
    if (report.isEmpty()) {
        System.out.println("No hay movimientos que mostrar en el reporte.");
        return;
    }

    System.out.println("Reporte de movimientos:");
    for (Transaction transaction : report) {
        String type = transaction.getType();
        String clientName = (transaction.getClient() != null) ? transaction.getClient().getProfile().getName() : "Cliente no especificado";
        String bookTitle = (transaction.getBook() != null) ? transaction.getBook().getTitle() : "Libro no especificado";
        String date = transaction.getDate().toString(); 
        System.out.println("Tipo: " + type + ", Cliente: " + clientName + ", Libro: " + bookTitle + ", Fecha: " + date);
    }
}


    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

}


    





