package com.example.vendorflow.data.notion

object HttpRoutes {
    private const val BASE_DATABASES_URL = "https://api.notion.com/v1/databases/"
    private const val BASE_USERS_URL = "https://api.notion.com/v1/users/"
    private const val PRODUCT_CATALOG_ID = "1b16e753951b80dabf2dd7947c4908aa"
    private const val PROJECTS_AND_TASKS_ID = "1b46e753951b802394dbe3ef95bd6ed6"

    const val PRODUCT_CATALOG = BASE_DATABASES_URL + PRODUCT_CATALOG_ID
    const val PROJECTS_AND_TASKS = BASE_DATABASES_URL + PROJECTS_AND_TASKS_ID
    const val USERS = BASE_USERS_URL
    const val BASE_PAGES_URL = "https://api.notion.com/v1/pages/"
}