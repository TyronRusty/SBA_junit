package sba.sms.services;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

public class CourseService implements CourseI {

	@Override
	public void createCourse(Course course) {
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
           
			tx = session.beginTransaction();
			session.persist(course);
			
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
	public Course getCourseById(int courseId) {
		Course course = null;
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
		tx = session.beginTransaction();
		course = session.get(Course.class, courseId);
		if (course == null) {
		throw new HibernateException("Course does not exist.");
		}
		tx.commit();
		} catch (HibernateException ex) {
		if (tx != null) {
		tx.rollback();
		throw ex;
		}
		} finally {
		session.close();
		}
		return course;
		}



	@Override
	public List<Course> getAllCourses() {
		Transaction tx = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Course> courselist = null;
        try {
            tx = session.beginTransaction();
            Query<Course> query = session.createQuery("from Course", Course.class);
            courselist = query.getResultList();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
                ex.printStackTrace();
            }
        } finally {
            session.close();
        }
        return courselist;
	}

}
