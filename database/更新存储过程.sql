DROP FUNCTION proc_task_item_bysearch(integer, integer, integer, integer, character varying, character varying);

CREATE OR REPLACE FUNCTION proc_task_item_bysearch(IN pageindex integer, IN recordcountperpage integer, IN status integer, IN userid integer, IN taskname character varying, IN processtype character varying, OUT allrecordcount integer, OUT curresult refcursor)
  RETURNS record AS
$BODY$
DECLARE
    vSQL1 varchar;
    vSQL2 varchar;
    vUsr varchar;
    limitpage varchar;
    vStatus varchar;
BEGIN 
    IF userid > 0 THEN
       vUsr = ' and qa_user_id = '|| userid;
    ELSE 
       vUsr = '';
    END IF;

    IF status = 0 THEN
       vStatus = ' status = 0 ';
    ELSE 
       vStatus  = ' status in (1,2)';
    END IF;

    IF recordcountperpage > 0 THEN
       limitpage = 'limit ' ||recordCountPerPage || ' offset ' ||recordCountPerPage*(pageIndex-1);
    ELSE 
       limitpage  = '';
    END IF;

    IF taskname !='' THEN
       taskname = ' and task_name like ''%' ||taskname ||'%''';
    ELSE 
       taskname  = '';
    END IF;

    IF processtype !='' THEN
       processtype = ' and process_type = ''' ||processtype || '''';
    ELSE 
       processtype  = '';
    END IF;

    vSQL1:='select count(*) from (select count(task_id) from task_item where '|| vStatus ||''|| vUsr ||''|| taskname ||''|| processtype || ' group by task_id,task_name) a';
    vSQL2:='select task_id taskid, task_name taskname from task_item where '|| vStatus ||''|| vUsr ||''|| taskname ||''|| processtype || '  group by task_id,task_name '|| limitpage ;
    execute vSQL1 into allRecordCount;
    OPEN curResult FOR EXECUTE vSQL2;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION proc_task_item_bysearch(integer, integer, integer, integer, character varying, character varying)
  OWNER TO postgres;
