// Vuex 때 처럼 create* 함수를 제공한다.
import { createWebHistory, createRouter } from 'vue-router';

const routes = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue'), // 동적 import
    },
    {
        path: '/event',
        name: 'Event',
        component: () => import('@/views/Event.vue'),
    },
    // {
    //     path: '/reservation',
    //     name: 'Reservation',
    //     component: () => import('@/views/ReservationView.vue'),
    // },
];

// 이렇게 해도 된다.
// const router = createRouter({
//   history: createWebHistory(),
//   routes,
// });
// export default router;

export const router = createRouter({
    history: createWebHistory(),
    routes,
});