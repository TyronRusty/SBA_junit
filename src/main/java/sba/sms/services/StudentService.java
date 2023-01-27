package sba.sms.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import jakarta.persistence.TypedQuery;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

public class StudentService implements StudentI  {

	@Override
	public List<Student> getAllStudents() {

		 Transaction tx = null;
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        List<Student> studentlist = null;
	        try {
	            tx = session.beginTransaction();
	            Query<Student> query = session.createQuery("from Student", Student.class);
	            studentlist = query.getResultList();
	            tx.commit();
	        } catch (HibernateException ex) {
	            if (tx != null) {
	                tx.rollback();
	                ex.printStackTrace();
	            }
	        } finally {
	            session.close();
	        }
	        return studentlist;
	}

	@Override
	public void createStudent(Student student) {
		
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {

			tx = session.beginTransaction();
			session.persist(student);
			tx.commit();

		} catch(
		HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally {
			session.close();
		}
	}

	@Override
	public Student getStudentByEmail(String email) {
		Student s = null;
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
		tx = session.beginTransaction();
		 TypedQuery <Student>  query = session.createQuery("from Student where email = :email", Student.class);
         query.setParameter("email", email);
         s = query.getSingleResult();
		if (s == null) {
		throw new HibernateException ("Student not found");
		}
		tx.commit();
		} catch (HibernateException ex) {
		ex.printStackTrace();
		tx.rollback();
		} catch (Exception ex) {
		ex.printStackTrace();
		} finally {
		session.close();
		}
		return s;





	}

	@Override
	public boolean validateStudent(String email, String password) {
		 Transaction tx = null;
		 Session session = HibernateUtil.getSessionFactory().openSession();
	     
	        boolean isValid=false;
	        try {
	            tx = session.beginTransaction();
	           Query<Student> query = session.createQuery("from Student where email = :email and password= :password", Student.class);
	            query.setParameter("email", email);
	            query.setParameter("password", password);

	            if(query.uniqueResult()==null) {
	                isValid = false;
	           
	            }
	            else {
	                isValid = true;
	            
	            }
	            tx.commit();
	        } catch (HibernateException exception) {
	            if (tx != null) tx.rollback();
	            exception.printStackTrace();
	        } finally {
	            session.close();
	        }

	        return isValid;
	}

	@Override
	public void registerStudentToCourse(String email, int courseId) {
			
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
		    tx = session.beginTransaction();
	         Course course = session.get(Course.class, courseId);
	         Student student = session.get(Student.class, email);
	         if (student != null && course != null && !student.getCourses().contains(course)) {
	             student.addCourse(course);
	             session.merge(student);
	             tx.commit();
	         } else {
	             throw new Exception("the student is already registered in the course.");
	         }
	     } catch (HibernateException ex) {
	         if (tx != null) {
	             tx.rollback();
	             ex.printStackTrace();
	         }
	     } catch (Exception ex) {
	         ex.printStackTrace();
	     } finally {
	         session.close();
	     }
	     }	
		
	

	@Override
	public List<Course> getStudentCourses(String email) {
		Transaction tx = null;
		List<Course> courselist = new ArrayList<>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
	
			tx = session.beginTransaction();

			TypedQuery<Course> query = session.createQuery("SELECT c FROM Course c JOIN c.students s WHERE s.email = :email", Course.class);
            query.setParameter("email", email);
            courselist = query.getResultList();
			tx.commit();

		} catch(
		HibernateException ex){
			ex.printStackTrace();
			tx.rollback();
		}finally {
			session.close();
		}
	
		return courselist;
	}

}
