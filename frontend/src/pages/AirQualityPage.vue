<script setup>
import { useAirQualityStore } from '@/stores/AirQualityStore.js';
import { DeleteOutlined } from '@ant-design/icons-vue';
import {MaBadge, MaModal, MaSpin, MaDatePicker, MaInput, MaNotification} from "@mobileaction/action-kit"
import "@mobileaction/action-kit/dist/style.css"
import {storeToRefs} from "pinia";

const store = useAirQualityStore();

const {
  city,
  startDate,
  endDate,
  cityResponse,
  results,
  showTable,
  isModalVisible,
  isLoading,
  recordToDelete,
} = storeToRefs(store);

const columns = store.columns;

const {handleGetData, getBadgeConfig, showConfirmModal, handleConfirm, handleCancel} = store;
</script>

<template>
  <div class="container">
    <h1 class="custom-header">
      Air Quality App
    </h1>
    <div class="inner-container">
      <ma-input v-model:value="city" class="custom-input" rows="1" size="large" type="textarea" placeholder="Enter city name..."></ma-input>
      <div class="start-end-date-container">
        <ma-date-picker v-model:value="startDate" placeholder="Start date"></ma-date-picker>
        <ma-date-picker v-model:value="endDate" placeholder="End date"></ma-date-picker>
      </div>
    </div>
    <button class="custom-ma-button" @click="handleGetData">Search Data</button>
    <div v-if="showTable" class="results-container">
      <div class="table-container">
        <h1 class="custom-small-header">
          {{ cityResponse }}'s Historical Air Quality Data
        </h1>
        <div class="table-wrapper">
          <a-table class="table" :data-source="results" :columns="columns" bordered>
            <template #date="{ text }">
              <span class="table-date">{{ text }}</span>
            </template>
            <template #coBadge="{ text }">
              <div class="badge-container">
                <ma-badge
                    :variant="getBadgeConfig(text).variant"
                    type="primary"
                    shape="dot"
                    size="medium"
                />
                <span class="badge-text">{{ text }}</span>
              </div>
            </template>
            <template #o3Badge="{ text }">
              <div class="badge-container">
                <ma-badge
                    :variant="getBadgeConfig(text).variant"
                    type="primary"
                    shape="dot"
                    size="medium"
                />
                <span class="badge-text">{{ text }}</span>
              </div>
            </template>
            <template #so2Badge="{ text }">
              <div class="badge-container">
                <ma-badge
                    :variant="getBadgeConfig(text).variant"
                    type="primary"
                    shape="dot"
                    size="medium"
                />
                <span class="badge-text">{{ text }}</span>
              </div>
            </template>
            <template #action="{ record }">
              <span @click="showConfirmModal(record)">
                <DeleteOutlined class="delete-icon" />
              </span>
            </template>
          </a-table>
        </div>
      </div>
    </div>
    <ma-modal
        v-if="isModalVisible"
        @cancel="handleCancel"
        @ok="handleConfirm"
        type="confirm"
        cancel-text="Cancel"
        ok-text="Yes, delete"
    >
      <template #title>Are you sure?</template>
      <template #content>
        This action will delete the air quality record for {{ cityResponse }} on {{ recordToDelete?.Date }}
        from the database.
      </template>
    </ma-modal>
    <div v-if="isLoading" class="loading-state">
      <ma-spin color="dark" type="round" spinning>
        <template #tip>Loading...</template>
      </ma-spin>
    </div>
  </div>
</template>

<style>
.container {
  @apply flex w-full p-5 justify-center flex-col items-center;
}

.custom-header {
  @apply text-blue-950 text-6xl font-bold mb-10;
}

.custom-small-header {
  @apply text-blue-950 text-3xl font-bold mb-4 text-left;
}

.badge-container {
  @apply flex items-center;
}

.start-end-date-container {
  @apply flex gap-2;
}

.badge-text {
  @apply ml-3 text-base;
}

.table-date {
  @apply text-base;
}

.table-wrapper {
  @apply w-full flex justify-center overflow-x-auto;
}

.inner-container {
  @apply flex flex-col md:flex-row shadow-xl p-4 justify-center md:items-center gap-4;
}

.custom-input {
  @apply w-80 md:w-64;
}

.results-container {
  @apply w-full flex justify-center;
}

.table-container {
  @apply max-w-screen-xl w-full mt-4 overflow-x-auto;
}

.table {
  @apply max-w-screen-xl w-full overflow-x-auto;
}

.custom-ma-button {
  @apply mt-10 bg-blue-950 rounded-xl text-white p-3;
}

.loading-state {
  @apply fixed inset-0 bg-gray-800 bg-opacity-50 flex items-center justify-center z-50;
}

.delete-icon {
  @apply text-red-500 cursor-pointer justify-center;
}
</style>