package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.interfaceImplementations.UnitOfWorkBase;
//import net.raphjava.studeeconsole.components.interfaces.*;
//import com.raphjava.softplanner.data.models.*;
//import com.raphjava.softplanner.data.models.fromTustantConsole.*;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Consumer;
//
//public class TCUnitOfWork<ExtrasGetter> extends UnitOfWorkBase<ExtrasGetter> implements UnitOfWork<ExtrasGetter>
//{
//    private SessionFactory sessionFactory;
//
//    private Session currentSession;
//
//
//    private TustantWeekRepository tustantWeekRepository;
//
//    private List<Runnable> onFlushedActions = new ArrayList<>();
//
//    public TustantWeekRepository getTustantWeekRepository()
//    {
//        return tustantWeekRepository;
//    }
//
//    private TopicSetupRepository topicSetupRepository;
//
//
//    public TopicSetupRepository getTopicSetupRepository()
//    {
//        return topicSetupRepository;
//    }
//
//    private TopicRepository topicRepository;
//
//
//    public TopicRepository getTopicRepository()
//    {
//        return topicRepository;
//    }
//
//    private TermRepository termRepository;
//
//
//    public TermRepository getTermRepository()
//    {
//        return termRepository;
//    }
//
//    private TeachingResourceRepository teachingResourceRepository;
//
//
//    public TeachingResourceRepository getTeachingResourceRepository()
//    {
//        return teachingResourceRepository;
//    }
//
//    private SchemeOfWorkWeekRepository schemeOfWorkWeekRepository;
//
//
//    public SchemeOfWorkWeekRepository getSchemeOfWorkWeekRepository()
//    {
//        return schemeOfWorkWeekRepository;
//    }
//
//    private SchemeOfWorkRepository schemeOfWorkRepository;
//
//
//    public SchemeOfWorkRepository getSchemeOfWorkRepository()
//    {
//        return schemeOfWorkRepository;
//    }
//
//    private EntityBaseRepository entityBaseRepository;
//
//    public EntityBaseRepository getEntityBaseRepository()
//    {
//        return entityBaseRepository;
//    }
//
//    private TimeSlotRepository timeSlotRepository;
//
//    public TimeSlotRepository getTimeSlotRepository()
//    {
//        return timeSlotRepository;
//    }
//
//    private LessonRepository lessonRepository;
//
//    public LessonRepository getLessonRepository()
//    {
//        return lessonRepository;
//    }
//
//    private SchoolEventRepository schoolEventRepository;
//
//    public SchoolEventRepository getSchoolEventRepository()
//    {
//        return schoolEventRepository;
//    }
//
//    private TermSetupRepository termSetupRepository;
//
//    public TermSetupRepository getTermSetupRepository()
//    {
//        return termSetupRepository;
//    }
//
//    private TimeSlotOwnerRepository timeSlotOwnerRepository;
//
//    public TimeSlotOwnerRepository getTimeSlotOwnerRepository()
//    {
//        return timeSlotOwnerRepository;
//    }
//
//    private UnitRepository unitRepository;
//
//    public UnitRepository getUnitRepository()
//    {
//        return unitRepository;
//    }
//
//    private UnitSetupRepository unitSetupRepository;
//
//    public UnitSetupRepository getUnitSetupRepository()
//    {
//        return unitSetupRepository;
//    }
//
//    Map<Class, TCRepositoryBaseArgs> repositoryArgs = new HashMap<>();
//
//    public TCUnitOfWork(SessionFactory sessionFactory, TCUtils tcUtils, EagerLoaderFactoryProvider e)
//    {
//        this.sessionFactory = sessionFactory;
//        Factory<EagerLoader<EntityBase>> es = e.create();
//        currentSession = sessionFactory.openSession();
//        currentSession.beginTransaction();
//        loadRepositories(tcUtils, e);
//        //TODO finish up with the TCUnitOfWork constructor. Done. Sometime in January 2019.
//    }
//
//    private void loadRepositories(TCUtils tcUtils, EagerLoaderFactoryProvider e)
//    {
//        //Raphtilities raphtilities = new Raphtilities();
//        //TCUtils tcUtils = new TCUtils(k, raphtilities);
//        HibernateMapping hibernateMapping = new HibernateMapping();
//        HibernateRepositoryArgs hibernateRepositoryArgs = new HibernateRepositoryArgs(() -> currentSession, hibernateMapping);
//
//        RaphtilityRepositoryBaseArgs<EntityBase> entBaseRaphtilityRepositoryBaseArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), EntityBase.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<EntityBase> baseArgs = new TCRepositoryBaseArgs(entBaseRaphtilityRepositoryBaseArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(EntityBase.class, baseArgs);
//        entityBaseRepository = new EntityBaseRepository(baseArgs);
//
//        RaphtilityRepositoryBaseArgs<SchemeOfWork> schemeOfWorkRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), SchemeOfWork.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<SchemeOfWork> sowArgs = new TCRepositoryBaseArgs<>(schemeOfWorkRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(SchemeOfWork.class, sowArgs);
//        schemeOfWorkRepository = new SchemeOfWorkRepository(sowArgs);
//
//        RaphtilityRepositoryBaseArgs<SchemeOfWorkWeek> schemeOfWorkWeekRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), SchemeOfWorkWeek.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<SchemeOfWorkWeek> sowWkArgs = new TCRepositoryBaseArgs<>(schemeOfWorkWeekRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(SchemeOfWorkWeek.class, sowWkArgs);
//        schemeOfWorkWeekRepository = new SchemeOfWorkWeekRepository(sowWkArgs);
//
//        RaphtilityRepositoryBaseArgs<TeachingResource> teachingResourceRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), TeachingResource.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<TeachingResource> tArgs = new TCRepositoryBaseArgs<>(teachingResourceRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(TeachingResource.class, tArgs);
//        teachingResourceRepository = new TeachingResourceRepository(tArgs);
//
//        RaphtilityRepositoryBaseArgs<Term> termRepoArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), Term.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<Term> tmArgs = new TCRepositoryBaseArgs<>(termRepoArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(Term.class, tmArgs);
//        termRepository = new TermRepository(tmArgs);
//
//        RaphtilityRepositoryBaseArgs<Lesson> lessonRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), Lesson.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<Lesson> lArgs = new TCRepositoryBaseArgs<>(lessonRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(Lesson.class, lArgs);
//        lessonRepository = new LessonRepository(lArgs);
//
//
//        RaphtilityRepositoryBaseArgs<SchoolEvent> schoolEventRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), SchoolEvent.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<SchoolEvent> seArgs = new TCRepositoryBaseArgs<>(schoolEventRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(SchoolEvent.class, seArgs);
//        schoolEventRepository = new SchoolEventRepository(seArgs);
//
//        RaphtilityRepositoryBaseArgs<TermSetup> termSetupRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), TermSetup.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<TermSetup> tsArgs = new TCRepositoryBaseArgs<>(termSetupRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(TermSetup.class, tsArgs);
//        termSetupRepository = new TermSetupRepository(tsArgs);
//
//        RaphtilityRepositoryBaseArgs<TimeSlot> timeSlotRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), TimeSlot.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<TimeSlot> tslArgs = new TCRepositoryBaseArgs<>(timeSlotRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(TimeSlot.class, tslArgs);
//        timeSlotRepository = new TimeSlotRepository(tslArgs);
//
//        RaphtilityRepositoryBaseArgs<TimeSlotOwner> timeSlotOwnerRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), TimeSlotOwner.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<TimeSlotOwner> tsloArgs = new TCRepositoryBaseArgs<>(timeSlotOwnerRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(TimeSlotOwner.class, tsloArgs);
//        timeSlotOwnerRepository = new TimeSlotOwnerRepository(tsloArgs);
//
//        RaphtilityRepositoryBaseArgs<Topic> topicRepoArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), Topic.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<Topic> toArgs = new TCRepositoryBaseArgs<>(topicRepoArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(Topic.class, toArgs);
//        topicRepository = new TopicRepository(toArgs);
//
//        RaphtilityRepositoryBaseArgs<TopicSetup> topicDetailRepoArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), TopicSetup.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<TopicSetup> tosArgs = new TCRepositoryBaseArgs<>(topicDetailRepoArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(TopicSetup.class, tosArgs);
//        topicSetupRepository = new TopicSetupRepository(tosArgs);
//
//        RaphtilityRepositoryBaseArgs<TustantWeek> tustantWeekRepoArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), TustantWeek.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<TustantWeek> tuArgs = new TCRepositoryBaseArgs<>(tustantWeekRepoArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(TustantWeek.class, tuArgs);
//        tustantWeekRepository = new TustantWeekRepository(tuArgs);
//
//        RaphtilityRepositoryBaseArgs<Unit> unitRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), Unit.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<Unit> uArgs = new TCRepositoryBaseArgs<>(unitRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(Unit.class, uArgs);
//        unitRepository = new UnitRepository(uArgs);
//
//        RaphtilityRepositoryBaseArgs<UnitSetup> unitSetupRepositoryArgs = new RaphtilityRepositoryBaseArgs<>(e.create(), UnitSetup.class, tcUtils.getEntityClassPropertyNames());
//        TCRepositoryBaseArgs<UnitSetup> usArgs = new TCRepositoryBaseArgs<>(unitSetupRepositoryArgs, hibernateRepositoryArgs, tcUtils);
//        repositoryArgs.put(UnitSetup.class, usArgs);
//        unitSetupRepository = new UnitSetupRepository(usArgs);
//
//    }
//
//    private void loadSessionFactory()
//    {
//        try
//        {
//            sessionFactory = new Configuration().configure().buildSessionFactory();
//        }
//        catch (Throwable e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public void registerOnFlushedActions(Runnable action)
//    {
//        onFlushedActions.add(action);
//    }
//
//    public void cud(Consumer<CRUD> createUpdateDeleteAction)
//    {
//        CreateUpdateDeleteAssistant cud = new CreateUpdateDeleteAssistant(this, repositoryArgs);
//        createUpdateDeleteAction.accept(cud);
//        cud.cascadePersistenceToHibernate();
//        flush();
//    }
//
//    @Override
//    public int complete()
//    {
//        //TODO Implement the complete method of this App's TCUnitOfWork. Done. Noted on 11th March 2019.
//        currentSession.getTransaction().commit();
//        sessionFactory = null;
//        currentSession.close();
//        currentSession = null;
//        System.out.println("Session closed.");
//        return 0;
//    }
//
//    @Override
//    public void flush()
//    {
//        currentSession.getTransaction().commit();
//        onFlushedActions.forEach(Runnable::run);
//        currentSession.close();
//        currentSession = sessionFactory.openSession();
//        currentSession.beginTransaction();
//    }
//}
