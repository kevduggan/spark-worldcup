CREATE KEYSPACE IF NOT EXISTS raw_logs_store WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 1 };

DROP TABLE IF EXISTS raw_logs_store.access_logs;
CREATE TABLE raw_logs_store.access_logs (
     day varchar,
     enterprise_id int,

     hour_offset int,
     hour_quadrant int,

     log_timestamp bigint,
     log_id uuid,

     account_ids set<int>,
     campaign_ids varchar,

     employee_account_ids set<int>,
     employee_email varchar,

     employee_department varchar,
     employee_region varchar,
     employee_country varchar,
     employee_paygrade varchar,
     employee_business_unit varchar,

     page_url varchar,
     page_title varchar,

     channel varchar,

     sharepoint_site_name varchar,

     nw_email_title varchar,

     browser_user_agent varchar,
     browser_language varchar,

     screen_resolution varchar,
     screen_colors varchar,

     request_ip varchar,
     request_referer varchar,

     PRIMARY KEY ((day, enterprise_id), hour_offset, log_id)
 )
 WITH CLUSTERING ORDER BY (hour_offset ASC);

CREATE KEYSPACE IF NOT EXISTS rep_audience_totals WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 1 };

DROP TABLE IF EXISTS rep_audience_totals.audience_totals;
CREATE TABLE rep_audience_totals.audience_totals(
    account_id int,
    campaign_code varchar,
    day varchar,
    granularity varchar,
    segment_type varchar,
    segment_value varchar,
    total int,
    PRIMARY KEY ((account_id,campaign_code,day,granularity),segment_type,segment_value)
);