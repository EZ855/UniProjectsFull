#include "./pipeline.h"

#include <catch2/catch.hpp>
#include <cstring>
#include <iostream>

TEST_CASE("pipeline error invalid_node_id complete functionality") {
    ppl::pipeline_error e {ppl::pipeline_error_kind::invalid_node_id};
    CHECK(e.kind() == ppl::pipeline_error_kind::invalid_node_id);
    CHECK(std::strcmp(e.what(), "invalid node ID") == 0);
}
TEST_CASE("pipeline error no_such_slot complete functionality") {
    ppl::pipeline_error e {ppl::pipeline_error_kind::no_such_slot};
    CHECK(e.kind() == ppl::pipeline_error_kind::no_such_slot);
    CHECK(std::strcmp(e.what(), "no such slot") == 0);
}
TEST_CASE("pipeline error slot_already_used complete functionality") {
    ppl::pipeline_error e {ppl::pipeline_error_kind::slot_already_used};
    CHECK(e.kind() == ppl::pipeline_error_kind::slot_already_used);
    CHECK(std::strcmp(e.what(), "slot already used") == 0);
}
TEST_CASE("pipeline error connection_type_mismatch complete functionality") {
    ppl::pipeline_error e {ppl::pipeline_error_kind::connection_type_mismatch};
    CHECK(e.kind() == ppl::pipeline_error_kind::connection_type_mismatch);
    CHECK(std::strcmp(e.what(), "connection type mismatch") == 0);
}
TEST_CASE("pipeline construction not crashing") {
    auto pipeline = ppl::pipeline{};
}
// Declaring user components
struct simple_source : ppl::source<int> {
	int current_value = 0;
	simple_source() = default;

	auto name() const -> std::string override {
		return "SimpleSource";
	}

	auto poll_next() -> ppl::poll override {
		if (current_value >= 10)
			return ppl::poll::closed;
		++current_value;
		return ppl::poll::ready;
	}

	auto value() const -> const int& override {
		return current_value;
	}
};
struct simple_sink : ppl::sink<int> {
	const ppl::producer<int>* slot0 = nullptr;

	simple_sink() = default;

	auto name() const -> std::string override {
		return "SimpleSink";
	}

	void connect(const ppl::node* src, int slot) override {
		if (slot == 0) {
			slot0 = static_cast<const ppl::producer<int>*>(src);
		}
	}

	auto poll_next() -> ppl::poll override {
		std::cout << slot0->value() << '\n';
		return ppl::poll::ready;
	}
};
struct simple_component : ppl::component<std::tuple<int>, int> {
	const ppl::producer<int>* slot0 = nullptr;
	int current_value = 0;
	simple_component() = default;

	auto name() const -> std::string override {
		return "SimpleComponent";
	}

	void connect(const ppl::node* src, int slot) override {
		if (slot == 0) {
			slot0 = static_cast<const ppl::producer<int>*>(src);
		}
	}

	auto poll_next() -> ppl::poll override {
		if (current_value < 0)
			return ppl::poll::closed;
		current_value = slot0->value();
		return ppl::poll::ready;
	}

	auto value() const -> const int& override {
		return current_value;
	}
};
struct two_input_component : ppl::component<std::tuple<int, int>, int> {
	const ppl::producer<int>* slot0 = nullptr;
	const ppl::producer<int>* slot1 = nullptr;
	int current_value = 0;
	two_input_component() = default;

	auto name() const -> std::string override {
		return "TwoInputComponent";
	}

	void connect(const ppl::node* src, int slot) override {
		if (slot == 0) {
			slot0 = static_cast<const ppl::producer<int>*>(src);
		}
		if (slot == 1) {
			slot1 = static_cast<const ppl::producer<int>*>(src);
		}
	}

	auto poll_next() -> ppl::poll override {
		if (current_value < 0)
			return ppl::poll::closed;
		current_value = slot0->value() + slot1->value();
		return ppl::poll::ready;
	}

	auto value() const -> const int& override {
		return current_value;
	}
};
TEST_CASE("create node not crashing") {
    auto pipeline = ppl::pipeline{};
	auto id = pipeline.create_node<simple_source>();
	CHECK(id == 0);
}
TEST_CASE("erase node not crashing") {
    auto pipeline = ppl::pipeline{};
	auto id = pipeline.create_node<simple_source>();
	pipeline.erase_node(id);
}
TEST_CASE("get node not crashing") {
    auto pipeline = ppl::pipeline{};
	auto id = pipeline.create_node<simple_source>();
	pipeline.get_node(id);
}
TEST_CASE("connect node not crashing") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto c_id = pipeline.create_node<simple_component>();
	auto sk_id = pipeline.create_node<simple_sink>();
	pipeline.connect(s_id, c_id, 0);
	pipeline.connect(c_id, sk_id, 0);
}
TEST_CASE("disconnect node not crashing") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto c_id = pipeline.create_node<simple_component>();
	pipeline.connect(s_id, c_id, 0);
	pipeline.disconnect(s_id, c_id);
}
TEST_CASE("get dependencies not crashing") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto c_id = pipeline.create_node<simple_component>();
	pipeline.connect(s_id, c_id, 0);
	auto ds = pipeline.get_dependencies(s_id);
	auto exp = std::pair<ppl::pipeline::node_id, int> {1, 0};
	auto expected = std::vector<std::pair<ppl::pipeline::node_id, int>> {exp};
	CHECK(ds == expected);
}
TEST_CASE("is valid working source slots not all filled") {
    auto pipeline = ppl::pipeline{};
	pipeline.create_node<simple_component>();
	auto f = pipeline.is_valid();
	CHECK(f);
}
TEST_CASE("is valid working one node no dependent") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto c_id = pipeline.create_node<simple_component>();
	pipeline.connect(s_id, c_id, 0);
	auto f = pipeline.is_valid();
	CHECK(f);
}
TEST_CASE("is valid working no sink") {
    auto pipeline = ppl::pipeline{};
	pipeline.create_node<simple_source>();
	auto c_id = pipeline.create_node<simple_component>();
	pipeline.connect(c_id, c_id, 0);
	auto f = pipeline.is_valid();
	CHECK(f);
}
TEST_CASE("is valid working subpipelines exist") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto c_id = pipeline.create_node<simple_component>();
	auto sk_id = pipeline.create_node<simple_sink>();
	pipeline.connect(s_id, c_id, 0);
	pipeline.connect(c_id, sk_id, 0);
	auto c_id2 = pipeline.create_node<simple_component>();
	pipeline.connect(c_id2, c_id2, 0);
	auto f = pipeline.is_valid();
	CHECK(f);
}
TEST_CASE("is valid working cycles exist") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto c_id = pipeline.create_node<two_input_component>();
	auto sk_id = pipeline.create_node<simple_sink>();
	pipeline.connect(s_id, c_id, 0);
	pipeline.connect(c_id, sk_id, 0);
	pipeline.connect(c_id, c_id, 0);
	auto f = pipeline.is_valid();
	CHECK(f);
}
TEST_CASE("is valid not crashing") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto sk_id = pipeline.create_node<simple_sink>();
	pipeline.connect(s_id, sk_id, 0);
	auto t = pipeline.is_valid();
	CHECK(t);
}
TEST_CASE("step not crashing") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto sk_id = pipeline.create_node<simple_sink>();
	pipeline.connect(s_id, sk_id, 0);
	auto t = pipeline.step();
	CHECK(t);
}
TEST_CASE("run not crashing") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto sk_id = pipeline.create_node<simple_sink>();
	pipeline.connect(s_id, sk_id, 0);
	pipeline.run();
}
TEST_CASE("print not crashing") {
    auto pipeline = ppl::pipeline{};
	auto s_id = pipeline.create_node<simple_source>();
	auto sk_id = pipeline.create_node<simple_sink>();
	pipeline.connect(s_id, sk_id, 0);
	std::cout << pipeline;
}