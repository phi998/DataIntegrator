import json

from job.JobsManager import JobsManager


def main(jobs_group_name, server_base_url):
    jm = JobsManager(server_base_url, jobs_group_name)

    jobs_ids = []

    jm.run_jobs()

    with open('jobs_in_progress.json', 'w') as json_file:
        json.dump(jobs_ids, json_file)


if __name__ == "__main__":
    main(jobs_group_name="jobs_mini", server_base_url="http://localhost:8080")
