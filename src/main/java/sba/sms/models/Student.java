package sba.sms.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import lombok.experimental.FieldDefaults;


@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "student")

public class Student {


    @Id
    @NonNull
	@Column(columnDefinition = "varchar(50)" ,name="email")
    String email;

    @NonNull
	@Column(columnDefinition = "varchar(50)" ,name="name")
    String name;

    @NonNull
	@Column(columnDefinition = "varchar(50)" ,name ="password")
    String password;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }
    
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE,CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @JoinTable(name = "student_courses",
    joinColumns = @JoinColumn(name="student_email"),
    inverseJoinColumns = @JoinColumn(name="course_id"))
    @Exclude
    Set<Course> courses = new TreeSet<>();

    public void addCourse(Course course) {
    	courses.add(course);
    	course.getStudents().add(this);
    }
}