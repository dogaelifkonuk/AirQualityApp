import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Air Pollution',
            component: () => import('../pages/AirQualityPage.vue')
        }
    ]
})

export default router