CREATE TABLE IF NOT EXISTS subscription (
    id INT NOT NULL CONSTRAINT subscription_pkey PRIMARY KEY,
    user_id INT NOT NULL CONSTRAINT subscription_user_id_fkey REFERENCES _user(id),
    subscription_type VARCHAR(255),
    verified_email BOOLEAN,
    recurring_payment BOOLEAN,
    one_time_payment BOOLEAN
);
