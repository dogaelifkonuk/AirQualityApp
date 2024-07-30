import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import axios from 'axios';
import { MaNotification } from '@mobileaction/action-kit';

export const useAirQualityStore = defineStore('airQuality', () => {
    const city = ref('');
    const startDate = ref('');
    const endDate = ref('');
    const cityResponse = ref('');
    const results = ref([]);
    const showTable = ref(false);
    const loading = ref(false);
    const isModalVisible = ref(false);
    const recordToDelete = ref(null);

    const columns = [
        {
            title: 'Date',
            dataIndex: 'Date',
            key: 'Date',
            width: '25%',
            slots: { customRender:  'date'}
        },
        {
            title: 'CO',
            dataIndex: ['Categories', 'CO'],
            key: 'CO',
            width: '25%',
            slots: { customRender: 'coBadge' }
        },
        {
            title: 'O3',
            dataIndex: ['Categories', 'O3'],
            key: 'O3',
            width: '25%',
            slots: { customRender: 'o3Badge' }
        },
        {
            title: 'SO2',
            dataIndex: ['Categories', 'SO2'],
            key: 'SO2',
            width: '25%',
            slots: { customRender: 'so2Badge' }
        },
        {
            key: 'operation',
            width: '10%',
            slots: { customRender: 'action' }
        }
    ];

    const isLoading = computed(() => {
        return loading.value;
    });

    const handleGetData = async () => {

        const today = new Date();
        const oneWeekAgo = new Date(today);
        oneWeekAgo.setDate(today.getDate() - 7);
        const earliestDate = new Date('2020-11-27');

        if (!city.value.trim()) {
            MaNotification.error({
                variant: "filled",
                description: "City name field is missing!",
                type: "error",
                title: 'Error',
                duration: 3000,
                placement: 'topRight'
            });
            return;
        }

        // Set default dates if startDate or endDate is empty
        if(!startDate.value.trim() && !endDate.value.trim()) {
            startDate.value = oneWeekAgo.toISOString().split('T')[0]; // Format as YYYY-MM-DD
            endDate.value = today.toISOString().split('T')[0]; // Format as YYYY-MM-DD
        }
        else if (!startDate.value.trim()) {
            startDate.value = earliestDate.toISOString().split('T')[0]; // Format as YYYY-MM-DD
        }
        else if (!endDate.value.trim()) {
            endDate.value = today.toISOString().split('T')[0]; // Format as YYYY-MM-DD
        }

        const startDateObj = new Date(startDate.value);
        if (startDateObj < earliestDate) {
            MaNotification.error({
                variant: "filled",
                description: "Start date can't be earlier than November 27, 2020.",
                type: "error",
                title: 'Error',
                duration: 3000,
                placement: 'topRight'
            });
            return;
        }

        if (startDate.value > endDate.value) {
            MaNotification.error({
                variant: "filled",
                description: "Start date can't be later than end date.",
                type: "error",
                title: 'Error',
                duration: 3000,
                placement: 'topRight'
            });
            return;
        }

        const endDateValue = new Date(endDate.value);
        if (endDateValue > today) {
            console.log(today);
            console.log(endDate.value);
            MaNotification.error({
                variant: "filled",
                description: "End date can't be later than today",
                type: "error",
                title: 'Error',
                duration: 3000,
                placement: 'topRight'
            });
            return;
        }

        loading.value = true;

        try {
            showTable.value = false;
            results.value = [];
            const response = await axios.get('http://localhost:8080/api/pollution/air-quality-data', {
                params: {
                    city: city.value,
                    startDate: startDate.value,
                    endDate: endDate.value
                }
            });
            console.log('Response:', response.data);

            const { City, Results } = response.data;
            cityResponse.value = City;

            results.value = Results;
            showTable.value = true;
        } catch (error) {
            if (error.response) {
                if (error.response.status === 500) {
                    MaNotification.error({
                        variant: "filled",
                        description: "Server error occured, please make sure to enter a valid city name!",
                        type: "error",
                        title: 'Error',
                        duration: 3000,
                        placement: 'topRight'
                    });
                }
            }
            console.error('Error:', error);
        }finally {
            loading.value = false;
        }
    };

    const getBadgeConfig = (content) => {
        switch (content.toLowerCase()) {
            case 'good':
                return { variant: 'green'};
            case 'satisfactory':
                return { variant: 'yellow'};
            case 'moderate':
                return { variant: 'orange'};
            case 'poor':
                return { variant: 'red'};
            case 'severe':
                return { variant: 'purple'};
            case 'hazardous':
                return { variant: 'dark'};
            default:
                return { variant: 'dark'};
        }
    };

    const showConfirmModal = (record) => {
        recordToDelete.value = record;
        isModalVisible.value = true;
    };

    const handleConfirm = async () => {
        const { Date: dateStr } = recordToDelete.value;
        const city = cityResponse.value;

        // Convert date from dd-mm-yyyy to yyyy-mm-dd
        const formattedDate = convertDateFormat(dateStr);

        // Send request to backend
        try {
            await axios.delete('http://localhost:8080/api/pollution/air-quality-data', {
                params: {
                    city: city,
                    startDate: formattedDate,
                    endDate: formattedDate
                }
            });
            MaNotification.success({
                variant: "filled",
                description: `Record for ${city} on ${dateStr} deleted successfully.`,
                type: "success",
                title: 'Success'
            });

            results.value = results.value.filter(item => item.Date !== dateStr);

        } catch (error) {
            console.error('Delete Error:', error);
            MaNotification.error({
                variant: "filled",
                description: `Failed to delete record for ${city} on ${dateStr}.`,
                type: "error",
                title: 'Error'
            });
        }
        isModalVisible.value = false;
    };

    const handleCancel = () => {
        isModalVisible.value = false;
    };

    const convertDateFormat = (dateStr) => {
        const [day, month, year] = dateStr.split('-').map(Number);
        return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
    };

    return {
        city,
        startDate,
        endDate,
        cityResponse,
        results,
        showTable,
        loading,
        isModalVisible,
        recordToDelete,
        columns,
        isLoading,
        handleGetData,
        getBadgeConfig,
        showConfirmModal,
        handleConfirm,
        handleCancel,
    };
});