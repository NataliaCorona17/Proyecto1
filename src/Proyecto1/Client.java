
package Proyecto1;

import java.util.ArrayList;

class Client {
    private Profile profile;
    private ArrayList<Book> borrowedBooks;

    public Client(Profile profile, ArrayList<Book> borrowedBooks) {
        this.profile = profile;
        this.borrowedBooks = borrowedBooks;
    }

   

    
    public Profile getProfile() {
        return profile;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

   
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setBorrowedBooks(ArrayList<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}