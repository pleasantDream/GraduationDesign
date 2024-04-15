import { defineStore } from 'pinia'
import { ref } from 'vue'

const usePhysicalStore = defineStore('physical', () => {
    // 定义状态相关的内容
    const physical = ref({});
    const setPhysical = (newPhysical) => {
        physical.value = newPhysical;
    }
    const removePhysical = () => {
        physical.value = {}
    }
    return { physical, setPhysical, removePhysical }
}, { persist: true })
export default usePhysicalStore;